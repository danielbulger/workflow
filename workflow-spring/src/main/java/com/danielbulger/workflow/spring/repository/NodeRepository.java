package com.danielbulger.workflow.spring.repository;

import com.danielbulger.workflow.spring.model.graph.Node;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeRepository extends JpaRepository<Node, Long> {
}
