package com.danielbulger.workflow.spring.service.db;

import com.danielbulger.workflow.spring.model.graph.Edge;
import com.danielbulger.workflow.spring.repository.EdgeRepository;
import com.danielbulger.workflow.spring.service.EdgeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DBEdgeService implements EdgeService {

	private final EdgeRepository edgeRepository;

	public DBEdgeService(EdgeRepository edgeRepository) {
		this.edgeRepository = edgeRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Edge> findById(long id) {
		return edgeRepository.findById(id);
	}

	@Override
	@Transactional
	public void save(Edge edge) {
		edgeRepository.save(edge);
	}

	@Override
	@Transactional
	public void delete(Edge edge) {
		edge.setActive(false);
		edgeRepository.save(edge);
	}
}
