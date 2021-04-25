package com.danielbulger.workflow.spring.service.db;

import com.danielbulger.workflow.spring.model.DefaultRole;
import com.danielbulger.workflow.spring.repository.DefaultRoleRepository;
import com.danielbulger.workflow.spring.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBRoleService implements RoleService {

	private final DefaultRoleRepository repository;

	public DBRoleService(DefaultRoleRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<DefaultRole> findAllDefaultRoles() {
		return repository.findAll();
	}
}
