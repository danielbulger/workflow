package com.danielbulger.workflow.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Config {

	private List<String> providers;
	private String authorizationRequestCookieName;
	private String redirectUriCookieName;
	private int cookieExpireTime;
	private List<String> authorizedRedirectUris;

	public List<String> getProviders() {
		return providers;
	}

	public List<String> getAuthorizedRedirectUris() {
		return authorizedRedirectUris;
	}

	public String getAuthorizationRequestCookieName() {
		return authorizationRequestCookieName;
	}

	public String getRedirectUriCookieName() {
		return redirectUriCookieName;
	}

	public int getCookieExpireTime() {
		return cookieExpireTime;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}

	public void setAuthorizationRequestCookieName(String authorizationRequestCookieName) {
		this.authorizationRequestCookieName = authorizationRequestCookieName;
	}

	public void setRedirectUriCookieName(String redirectUriCookieName) {
		this.redirectUriCookieName = redirectUriCookieName;
	}

	public void setCookieExpireTime(int cookieExpireTime) {
		this.cookieExpireTime = cookieExpireTime;
	}

	public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
		this.authorizedRedirectUris = authorizedRedirectUris;
	}
}
