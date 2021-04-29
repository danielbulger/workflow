package com.danielbulger.workflow.spring.repository;

import com.danielbulger.workflow.spring.model.graph.Edge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
}
