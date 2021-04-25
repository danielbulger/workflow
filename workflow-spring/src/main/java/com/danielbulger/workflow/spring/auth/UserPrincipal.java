package com.danielbulger.workflow.spring.auth;

import com.danielbulger.workflow.spring.model.Role;
import com.danielbulger.workflow.spring.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserPrincipal implements OAuth2User, UserDetails {

	public static UserPrincipal create(User user) {
		return create(user, new HashMap<>());
	}

	public static UserPrincipal create(User user, Map<String, Object> attributes) {
		return new UserPrincipal(
			user,
			attributes
		);
	}

	private final User user;
	private final Map<String, Object> attributes;

	public UserPrincipal(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	public String getEmail() {
		return user.getEmail();
	}

	@Override
	public String getPassword() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles()
			.stream()
			.map(Role::toGrantedAuthority)
			.collect(Collectors.toList());
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return user.getEmail();
	}

	public User getUser() {
		return user;
	}
}
