package com.danielbulger.workflow;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Node {

	private static final AtomicInteger counter = new AtomicInteger(0);

	private final int id;

	private final GraphExecutor executor;

	private final Map<Property, Buffer<?>> inputs = new ConcurrentHashMap<>();

	private final Map<Property, Collection<Edge>> edges = new ConcurrentHashMap<>();

	public Node(@NotNull GraphExecutor executor) {
		this.id = counter.incrementAndGet();
		this.executor = Objects.requireNonNull(executor);
	}

	public abstract void execute() throws NodeExecutionException;

	public abstract Set<Property> getInputProperties();

	public abstract Set<Property> getOutputProperties();

	public void addInput(@NotNull Property property, @NotNull Buffer<?> buffer) {

		if (inputs.containsKey(property)) {
			throw new IllegalStateException("Property has already been registered");
		}

		inputs.put(property, buffer);
	}

	public void addOutput(@NotNull Property property) {

		if (edges.containsKey(property)) {
			throw new IllegalStateException("Property has already been registered");
		}

		edges.put(property, new CopyOnWriteArraySet<>());

	}

	public void connect(@NotNull Property property, @NotNull final Edge edge) {
		final Collection<Edge> propertyEdges = edges.get(property);

		if (propertyEdges == null) {
			throw new IllegalArgumentException("Unknown output property " + property);
		}

		propertyEdges.add(edge);
	}

	protected <T> void send(final @NotNull Property property, final @NotNull T value) {

		Objects.requireNonNull(property);

		Objects.requireNonNull(value);

		final Collection<Edge> propertyEdge = edges.get(property);

		if (propertyEdge == null) {
			throw new IllegalArgumentException("Unknown output property " + property);
		}

		for (final Edge edge : propertyEdge) {
			edge.transfer(value);
		}

	}

	public <T> void push(final @NotNull Property property, final @NotNull T value) {

		Objects.requireNonNull(value);

		Objects.requireNonNull(property);

		if (!property.isAllowedType(value.getClass())) {
			throw new IllegalArgumentException("Invalid type for Property: " + property);
		}

		getInput(property).insert(value);

		// Since we have received some data, we need to let the graph executor
		// know that we can try and run.
		executor.schedule(this);
	}

	@Contract(pure = true)
	@SuppressWarnings("unchecked")
	protected <T> Buffer<T> getInput(final @NotNull Property property) {

		Objects.requireNonNull(property);

		final Buffer<?> buffer = inputs.get(property);

		if (buffer == null) {
			throw new IllegalArgumentException("Unknown input property");
		}

		return (Buffer<T>) buffer;
	}

	public void detach() {
		inputs.clear();

		edges.clear();
	}

	@Contract(pure = true)
	public boolean isSelfStarting() {
		return inputs.isEmpty();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Node node = (Node) o;
		return id == node.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
