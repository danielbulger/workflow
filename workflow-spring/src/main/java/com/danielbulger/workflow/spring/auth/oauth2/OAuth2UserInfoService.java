package com.danielbulger.workflow.spring.auth.oauth2;

import com.danielbulger.workflow.spring.auth.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.danielbulger.workflow.spring.auth.oauth2.provider.GithubOAuth2UserInfo;
import com.danielbulger.workflow.spring.config.OAuth2Config;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2UserInfoService {

	private final OAuth2Config oAuth2Config;

	public OAuth2UserInfoService(OAuth2Config oAuth2Config) {
		this.oAuth2Config = oAuth2Config;
	}

	public OAuth2UserInfo getOAuth2UserInfo(String providerId, Map<String, Object> attributes) throws Exception {
		switch(providerId.toLowerCase()) {
			case "github":
				return new GithubOAuth2UserInfo(attributes);
			default:
				throw new OAuth2AuthenticationProcessingException();
		}
	}
}
