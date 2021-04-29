package com.danielbulger.workflow.spring.service;

import com.danielbulger.workflow.spring.model.graph.Node;

import java.util.Optional;

public interface NodeService {

	Optional<Node> findById(long id);

	void save(Node node);

	void delete(Node node);
}
