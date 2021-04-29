package com.danielbulger.workflow.spring.service;

import com.danielbulger.workflow.spring.model.graph.Edge;

import java.util.Optional;

public interface EdgeService {

	Optional<Edge> findById(long id);

	void save(Edge edge);

	void delete(Edge edge);
}
