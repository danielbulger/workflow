package com.danielbulger.workflow.spring.service;

import com.danielbulger.workflow.spring.model.DefaultRole;

import java.util.List;

public interface RoleService {

	List<DefaultRole> findAllDefaultRoles();
}
