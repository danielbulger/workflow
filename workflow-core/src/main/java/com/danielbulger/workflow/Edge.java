package com.danielbulger.workflow;

import java.util.Objects;

public class Edge {

	private final Node node;

	private final Property property;

	public Edge(
		Node node,
		Property property
	) {
		this.node = Objects.requireNonNull(node);
		this.property = Objects.requireNonNull(property);
	}

	public void transfer(Object element) {

		if (!property.isAllowedType(element.getClass())) {
			throw new IllegalArgumentException("Invalid type for property");
		}

		node.push(property, element);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Edge edge = (Edge) o;
		return node.equals(edge.node) &&
			property.equals(edge.property);
	}

	@Override
	public int hashCode() {
		return Objects.hash(node, property);
	}
}
