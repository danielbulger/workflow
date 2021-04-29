package com.danielbulger.workflow.spring.model.graph;

import javax.persistence.*;

@Entity
public class Port {

	public enum Direction {
		Incoming, Outgoing
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(optional = false)
	private Node node;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private Direction direction;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
