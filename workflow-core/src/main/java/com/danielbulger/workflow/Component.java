package com.danielbulger.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Component {

	private static String normalisePortName(String name) {
		return name.toUpperCase();
	}

	private final Map<String, Buffer<?>> inputPorts = new ConcurrentHashMap<>();

	private final Map<String, Collection<Connection>> outputConnections = new ConcurrentHashMap<>();

	private final ComponentId id;

	private final Network network;

	private boolean detached = false;

	private final Lock lock = new ReentrantLock();

	public Component(Network network) {
		this.id = ComponentId.next();

		this.network = network;
	}

	protected void doExecute() {
		try {
			lock.lock();
			execute();
		} finally {
			lock.unlock();
		}
	}

	public abstract void execute();

	public void addInputPort(String name, Buffer<?> buffer) {
		inputPorts.put(normalisePortName(name), buffer);
	}

	public void addOutputConnection(String portName, Connection connection) {

		final String newPortName = normalisePortName(portName);

		Collection<Connection> list = outputConnections.computeIfAbsent(newPortName, k -> new ArrayList<>());

		list.add(connection);
	}

	public void removeConnection(String portName, Connection connection) {
		final Collection<Connection> connections = outputConnections.get(normalisePortName(portName));

		connections.remove(connection);
	}

	public <T> void receive(String portName, T value) throws Exception {
		if (detached) {
			throw new Exception();
		}

		final Buffer<T> buffer = getBuffer(portName);

		buffer.insert(value);

		// Since we have received some data, we need to let the network know that we can try and run.
		network.schedule(this);
	}

	public void send(String portName, Object value) {
		final Collection<Connection> list = outputConnections.get(normalisePortName(portName));

		if (list == null) {
			return;
		}

		for (final Connection connection : list) {
			connection.transfer(value);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> Buffer<T> getBuffer(String portName) {

		final Buffer<?> buffer = inputPorts.get(normalisePortName(portName));

		if (buffer == null) {
			throw new IllegalArgumentException("Unknown port: " + portName);
		}

		return (Buffer<T>) buffer;
	}

	public void detach() {
		detached = true;

		inputPorts.clear();

		outputConnections.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Component component = (Component) o;
		return id.equals(component.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Component{" +
			"id=" + id +
			'}';
	}
}