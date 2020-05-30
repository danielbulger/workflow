package com.danielbulger.workflow;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class Node {

	private final Map<Property, Buffer<?>> inputs = new ConcurrentHashMap<>();

	private final Map<Property, Collection<Edge>> edges = new ConcurrentHashMap<>();

	@Contract(mutates = "this")
	public abstract void execute() throws NodeExecutionException;

	@Contract(mutates = "this")
	public void addInput(@NotNull Property property, @NotNull Buffer<?> buffer) {

		if(inputs.containsKey(property)) {
			throw new IllegalStateException("Property has already been registered");
		}

		inputs.put(property, buffer);
	}

	@Contract(mutates = "this")
	public void addOutput(@NotNull Property property) {

		if(edges.containsKey(property)) {
			throw new IllegalStateException("Property has already been registered");
		}

		edges.put(property, new CopyOnWriteArraySet<>());

	}

	@Contract(mutates = "this")
	public void connect(@NotNull Property property, @NotNull final Edge edge) {
		final Collection<Edge> propertyEdges = edges.get(property);

		if(propertyEdges == null) {
			throw new IllegalArgumentException("Unknown output property " + property);
		}

		propertyEdges.add(edge);
	}

	@Contract(mutates = "this")
	protected <T> void send(final @NotNull Property property, final @NotNull T value) {
		final Collection<Edge> propertyEdge = edges.get(property);

		if(propertyEdge == null) {
			throw new IllegalArgumentException("Unknown output property " + property);
		}

		for(final Edge edge : propertyEdge) {
			edge.transfer(value);
		}

	}

	@SuppressWarnings("unchecked")
	@Contract(mutates = "this")
	public <T> void push(final @NotNull Property property, final @NotNull T value) {

		Objects.requireNonNull(value);

		if (!property.isAllowedType(value.getClass())) {
			throw new IllegalArgumentException("Invalid type for Property: " + property);
		}

		getInput(property).insert(value);
	}

	@Contract(pure = true)
	protected <T> Buffer<T> getInput(final @NotNull Property property) {

		Objects.requireNonNull(property);

		final Buffer<?> buffer = inputs.get(property);

		if(buffer == null) {
			throw new IllegalArgumentException("Unknown input property");
		}

		return (Buffer<T>) buffer;
	}
}
