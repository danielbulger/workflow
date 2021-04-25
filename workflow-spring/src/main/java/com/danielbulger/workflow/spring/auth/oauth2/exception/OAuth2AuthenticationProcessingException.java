package com.danielbulger.workflow.spring.auth.oauth2.exception;

import javax.naming.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {
	public OAuth2AuthenticationProcessingException(String explanation) {
		super(explanation);
	}

	public OAuth2AuthenticationProcessingException() {
		super();
	}
}
