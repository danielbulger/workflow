package com.danielbulger.workflow.spring.service;

import com.danielbulger.workflow.spring.model.user.User;

import java.util.Optional;

public interface UserService {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	User save(User user);
}
