package com.danielbulger.workflow.spring.auth.oauth2.provider;

import com.danielbulger.workflow.spring.auth.oauth2.OAuth2UserInfo;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

	public GithubOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return ((Integer) attributes.get("id")).toString();
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getAvatarUrl() {
		return (String) attributes.get("avatar_url");
	}
}
