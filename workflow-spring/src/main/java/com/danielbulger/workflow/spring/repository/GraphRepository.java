package com.danielbulger.workflow.spring.repository;

import com.danielbulger.workflow.spring.model.graph.Graph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraphRepository extends JpaRepository<Graph, Long> {
}
