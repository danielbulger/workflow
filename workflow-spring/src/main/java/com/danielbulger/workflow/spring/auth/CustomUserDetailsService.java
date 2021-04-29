package com.danielbulger.workflow.spring.auth;

import com.danielbulger.workflow.spring.model.user.User;
import com.danielbulger.workflow.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(CustomUserDetailsService.class);

	private final UserService userService;

	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userService.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found " + username));

		LOG.debug("User {} found", username);
		return UserPrincipal.create(user);
	}
}
