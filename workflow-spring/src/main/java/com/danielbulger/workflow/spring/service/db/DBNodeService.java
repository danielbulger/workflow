package com.danielbulger.workflow.spring.service.db;

import com.danielbulger.workflow.spring.model.graph.Node;
import com.danielbulger.workflow.spring.repository.NodeRepository;
import com.danielbulger.workflow.spring.service.NodeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DBNodeService implements NodeService {

	private final NodeRepository nodeRepository;

	public DBNodeService(NodeRepository nodeRepository) {
		this.nodeRepository = nodeRepository;
	}

	@Override
	public Optional<Node> findById(long id) {
		return nodeRepository.findById(id);
	}

	@Override
	public void save(Node node) {
		nodeRepository.save(node);
	}

	@Override
	public void delete(Node node) {
		node.setActive(false);
		nodeRepository.save(node);
	}
}
