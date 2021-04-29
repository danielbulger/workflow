package com.danielbulger.workflow.spring.service.db;

import com.danielbulger.workflow.spring.model.graph.Graph;
import com.danielbulger.workflow.spring.repository.GraphRepository;
import com.danielbulger.workflow.spring.service.GraphService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DBGraphService implements GraphService {
	private final GraphRepository graphRepository;

	public DBGraphService(GraphRepository graphRepository) {
		this.graphRepository = graphRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Graph> findById(long id) {
		return graphRepository.findById(id);
	}

	@Override
	@Transactional
	public void save(Graph graph) {
		graphRepository.save(graph);
	}

	@Override
	@Transactional
	public void delete(Graph graph) {
		graph.setActive(false);
		graphRepository.save(graph);
	}
}
