package com.danielbulger.workflow.spring.repository;

import com.danielbulger.workflow.spring.model.DefaultRole;
import com.danielbulger.workflow.spring.model.Role;
import com.danielbulger.workflow.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
