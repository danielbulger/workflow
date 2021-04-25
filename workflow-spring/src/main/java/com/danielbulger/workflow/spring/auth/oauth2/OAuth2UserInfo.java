package com.danielbulger.workflow.spring.auth.oauth2;

import java.util.Map;

public abstract class OAuth2UserInfo {

	protected final Map<String, Object> attributes;

	protected OAuth2UserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public abstract String getId();

	public abstract String getName();

	public abstract String getEmail();

	public abstract String getAvatarUrl();
}
