package com.danielbulger.workflow;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Edge {

	private final Node node;

	private final Property property;

	public Edge(
		@NotNull Node node,
		@NotNull Property property
	) {
		this.node = Objects.requireNonNull(node);
		this.property = Objects.requireNonNull(property);
	}

	@Contract(mutates = "this")
	public void transfer(@NotNull Object element) {
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
