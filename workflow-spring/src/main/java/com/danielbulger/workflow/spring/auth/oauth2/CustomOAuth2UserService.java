package com.danielbulger.workflow.spring.auth.oauth2;

import com.danielbulger.workflow.spring.auth.UserPrincipal;
import com.danielbulger.workflow.spring.auth.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.danielbulger.workflow.spring.model.DefaultRole;
import com.danielbulger.workflow.spring.model.User;
import com.danielbulger.workflow.spring.service.RoleService;
import com.danielbulger.workflow.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomOAuth2UserService.class);

	private final UserService userService;
	private final OAuth2UserInfoService userInfoService;
	private final RoleService roleService;

	public CustomOAuth2UserService(
		UserService userService,
		OAuth2UserInfoService userInfoService,
		RoleService roleService
	) {
		this.userService = userService;
		this.userInfoService = userInfoService;
		this.roleService = roleService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		try {
			return processOAuth2User(userRequest, super.loadUser(userRequest));
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) throws Exception {
		final OAuth2UserInfo userInfo = userInfoService.getOAuth2UserInfo(
			userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes()
		);

		User user;
		final Optional<User> userOptional = userService.findByEmail(userInfo.getEmail());

		if (userOptional.isPresent()) {
			user = userOptional.get();

			final String registrationId = userRequest.getClientRegistration().getRegistrationId();
			// User has logged in with different provider than the one they registered for.
			if (!user.getProvider().equals(registrationId)) {
				LOG.debug(
					"User {} attempted to login with provider {} instead of {}",
					user.getEmail(),
					user.getProvider(),
					registrationId
				);
				throw new OAuth2AuthenticationProcessingException();
			}
			user = updateExistingUser(user, userInfo);
		} else {
			user = registerNewUser(userRequest, userInfo);
		}

		return UserPrincipal.create(user, oAuth2User.getAttributes());
	}

	private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
		final User user = new User();
		user.setProvider(userRequest.getClientRegistration().getRegistrationId());
		user.setProviderId(userInfo.getId());
		user.setName(userInfo.getName());
		user.setEmail(userInfo.getEmail());
		user.setAvatarUrl(userInfo.getAvatarUrl());
		user.setRoles(roleService
			.findAllDefaultRoles()
			.stream()
			.map(DefaultRole::getRole).collect(Collectors.toList()));
		LOG.debug("Creating user {}", user);
		return userService.save(user);
	}

	private User updateExistingUser(User user, OAuth2UserInfo userInfo) {
		user.setName(userInfo.getName());
		user.setAvatarUrl(userInfo.getAvatarUrl());
		LOG.debug("Updating user {} name={}, avatar={}", user.getEmail(), user.getName(), user.getAvatarUrl());
		return userService.save(user);
	}
}
