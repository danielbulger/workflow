package com.danielbulger.workflow.spring.auth.oauth2;

import com.danielbulger.workflow.spring.config.OAuth2Config;
import com.danielbulger.workflow.spring.model.http.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OAuth2AuthenticationFailureHandler.class);

	private final OAuth2Config oAuth2Config;
	private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;

	public OAuth2AuthenticationFailureHandler(
		OAuth2Config oAuth2Config,
		HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository
	) {
		this.oAuth2Config = oAuth2Config;
		this.authorizationRequestRepository = authorizationRequestRepository;
	}

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception
	) throws IOException, ServletException {
		String targetUrl = Cookies.get(request, oAuth2Config.getRedirectUriCookieName())
			.map(Cookie::getValue)
			.orElse("/");

		targetUrl = UriComponentsBuilder
			.fromUriString(targetUrl)
			.build()
			.toUriString();

		LOG.debug("Authentication Failure: error={}, targetUrl={}", exception, targetUrl);

		authorizationRequestRepository.removeAuthorizationRequest(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
