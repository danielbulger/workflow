package com.danielbulger.workflow.spring.service.db;

import com.danielbulger.workflow.spring.model.Role;
import com.danielbulger.workflow.spring.model.User;
import com.danielbulger.workflow.spring.repository.UserRepository;
import com.danielbulger.workflow.spring.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class DBUserService implements UserService {

	private final UserRepository userRepository;

	public DBUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}

}
