package com.danielbulger.workflow.spring.auth.oauth2;

import com.danielbulger.workflow.spring.config.OAuth2Config;
import com.danielbulger.workflow.spring.model.http.Cookies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

	private static final Logger LOG = LoggerFactory.getLogger(HttpCookieOAuth2AuthorizationRequestRepository.class);
	private final OAuth2Config oAuth2Config;

	public HttpCookieOAuth2AuthorizationRequestRepository(OAuth2Config oAuth2Config) {
		this.oAuth2Config = oAuth2Config;
	}

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		return Cookies.get(request, oAuth2Config.getAuthorizationRequestCookieName())
			.map(cookie -> Cookies.deserialize(cookie, OAuth2AuthorizationRequest.class))
			.orElse(null);
	}

	@Override
	public void saveAuthorizationRequest(
		OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		if (authorizationRequest == null) {
			LOG.debug("No authorization request");
			Cookies.delete(request, response, oAuth2Config.getAuthorizationRequestCookieName());
			Cookies.delete(request, response, oAuth2Config.getRedirectUriCookieName());
			return;
		}

		Cookies.add(
			response,
			oAuth2Config.getAuthorizationRequestCookieName(),
			Cookies.serialize(authorizationRequest),
			oAuth2Config.getCookieExpireTime()
		);

		LOG.debug("Adding authorization cookie name={}", oAuth2Config.getAuthorizationRequestCookieName());

		final String redirectUriAfterLogin = request.getParameter(oAuth2Config.getRedirectUriCookieName());
		if (redirectUriAfterLogin != null && !redirectUriAfterLogin.isEmpty()) {
			LOG.debug("Adding redirect cookie name={}, value={}",
				oAuth2Config.getRedirectUriCookieName(),
				redirectUriAfterLogin
			);

			Cookies.add(
				response,
				oAuth2Config.getRedirectUriCookieName(),
				redirectUriAfterLogin,
				oAuth2Config.getCookieExpireTime()
			);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return this.loadAuthorizationRequest(request);
	}

	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug(
			"Removing cookie auth={}, redirect={}",
			oAuth2Config.getAuthorizationRequestCookieName(),
			oAuth2Config.getRedirectUriCookieName()
		);
		Cookies.delete(request, response, oAuth2Config.getAuthorizationRequestCookieName());
		Cookies.delete(request, response, oAuth2Config.getRedirectUriCookieName());
	}
}
