package com.danielbulger.workflow.spring.auth.oauth2;

import com.danielbulger.workflow.spring.auth.oauth2.exception.BadRequestException;
import com.danielbulger.workflow.spring.auth.token.TokenProvider;
import com.danielbulger.workflow.spring.config.OAuth2Config;
import com.danielbulger.workflow.spring.http.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

	private final TokenProvider tokenProvider;
	private final OAuth2Config oAuth2Config;
	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

	public OAuth2AuthenticationSuccessHandler(
		TokenProvider tokenProvider,
		OAuth2Config oAuth2Config,
		HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository
	) {
		this.tokenProvider = tokenProvider;
		this.oAuth2Config = oAuth2Config;
		this.authorizationRequestRepository = authorizationRequestRepository;
	}

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain,
		Authentication authentication
	) throws IOException, ServletException {
		if (response.isCommitted()) {
			LOG.debug("Response already committed");
			return;
		}

		final String targetUrl = determineTargetUrl(request, response, authentication);

		clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	@Override
	protected String determineTargetUrl(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) {
		final Optional<String> redirectUri = Cookies.get(request, oAuth2Config.getRedirectUriCookieName())
			.map(Cookie::getValue);

		if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
			throw new BadRequestException("Unauthorized Redirect URI");
		}

		final String token = tokenProvider.createToken(authentication);
		tokenProvider.createCookie(response, token);

		return UriComponentsBuilder.fromUriString(redirectUri.orElse(getDefaultTargetUrl()))
			.build()
			.toUriString();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}

	private boolean isAuthorizedRedirectUri(String uri) {
		final URI clientRedirectUri = URI.create(uri);
		return oAuth2Config.getAuthorizedRedirectUris()
			.stream()
			.anyMatch(redirectUri -> {
				final URI authorizedURI = URI.create(redirectUri);
				return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
					&& authorizedURI.getPort() == clientRedirectUri.getPort();
			});
	}
}
