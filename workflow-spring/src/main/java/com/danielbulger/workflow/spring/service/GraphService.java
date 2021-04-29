package com.danielbulger.workflow.spring.service;

import com.danielbulger.workflow.spring.model.graph.Graph;

import java.util.Optional;

public interface GraphService {

	Optional<Graph> findById(long id);

	void save(Graph graph);

	void delete(Graph graph);
}
