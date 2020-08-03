package com.danielbulger.workflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Component {

	/**
	 * Normalises the given port name.
	 *
	 * @param name The port name to normalise.
	 * @return The normalised port name.
	 */
	private static String normalisePortName(String name) {
		return name.toUpperCase();
	}

	private final Map<String, Buffer<?>> inputPorts = new ConcurrentHashMap<>();

	private final Map<String, Collection<Connection>> outputConnections = new ConcurrentHashMap<>();

	private final ComponentId id;

	private final Network network;

	private boolean detached = false;

	private final Lock lock = new ReentrantLock();

	/**
	 * Creates a new Component for the given {@code network}.
	 *
	 * @param network The {@code Network} that this component will be apart of.
	 */
	public Component(Network network) {
		this.id = ComponentId.next();

		this.network = network;
	}

	/**
	 * Executes this component.
	 */
	protected void doExecute() {
		try {
			lock.lock();
			execute();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Runs the component logic.
	 */
	public abstract void execute();

	/**
	 * Adds a new input port.
	 *
	 * @param portName The name of the input port.
	 * @param buffer   The buffer that will store the input port data.
	 * @throws IllegalArgumentException If {@code portName} is {@code null} or {@code empty}, or {@code buffer} is {@code null}.
	 */
	public void addInputPort(String portName, Buffer<?> buffer) {
		if (portName == null || portName.isBlank()) {
			throw new IllegalArgumentException("Invalid portName " + portName);
		}

		if (buffer == null) {
			throw new IllegalArgumentException("buffer is null");
		}

		inputPorts.put(normalisePortName(portName), buffer);
	}

	/**
	 * Adds a new output port.
	 *
	 * @param portName   The name of the output port.
	 * @param connection The connection that handles the outgoing traffic.
	 * @throws IllegalArgumentException If {@code portName} is {@code null} or {@code empty}, or {@code connection} is {@code null}.
	 */
	public void addOutputConnection(String portName, Connection connection) {

		if (portName == null || portName.isBlank()) {
			throw new IllegalArgumentException("Invalid portName " + portName);
		}

		if (connection == null) {
			throw new IllegalArgumentException("connection is null");
		}

		final String newPortName = normalisePortName(portName);

		Collection<Connection> list = outputConnections.computeIfAbsent(newPortName, k -> new ArrayList<>());

		list.add(connection);
	}

	/**
	 * Removes an existing connection, if possible, from the output port.
	 *
	 * @param portName   The name of the output port.
	 * @param connection The connection to remove.
	 * @return {@code true} if the connection was removed, {@code false} otherwise.
	 * @throws IllegalArgumentException If {@code portName} is {@code null} or {@code empty}, or {@code connection} is {@code null}.
	 */
	public boolean removeConnection(String portName, Connection connection) {
		if (portName == null || portName.isBlank()) {
			throw new IllegalArgumentException("Invalid portName " + portName);
		}

		if (connection == null) {
			throw new IllegalArgumentException("connection is null");
		}

		final Collection<Connection> connections = outputConnections.get(normalisePortName(portName));

		return connections.remove(connection);
	}

	/**
	 * Accepts the input {@code value} into the port identified by {@code portName}.
	 *
	 * @param portName The port that transfer the data to.
	 * @param value    The data that will be transferred.
	 * @param <T>      The type of the data.
	 * @throws Exception                If the component is no longer accepting input.
	 * @throws IllegalArgumentException If {@code portName} is {@code null} or {@code empty}.
	 * @throws IllegalArgumentException If {@code value} is {@code null}.
	 * @throws IllegalArgumentException If there does not exist an input port identified by {@code portName}.
	 */
	public <T> void receive(String portName, T value) throws Exception {
		if (detached) {
			throw new Exception();
		}

		if (portName == null || portName.isBlank()) {
			throw new IllegalArgumentException("Invalid portName " + portName);
		}

		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}

		final Buffer<T> buffer = getBuffer(portName);

		buffer.insert(value);

		// Since we have received some data, we need to let the network know that we can try and run.
		network.schedule(this);
	}

	/**
	 * Sends data from the output port identified by {@code portName}.
	 *
	 * @param portName The name of the output port.
	 * @param value    The data that will be sent.
	 * @throws IllegalArgumentException If there does not exist an output port identified by {@code portName}.
	 */
	public void send(String portName, Object value) {
		final Collection<Connection> list = outputConnections.get(normalisePortName(portName));

		if (list == null) {
			return;
		}

		for (final Connection connection : list) {
			connection.transfer(value);
		}
	}

	/**
	 * Retrieves the input port buffer for the port identified by {@code portName}.
	 *
	 * @param portName The name of the input port.
	 * @param <T>      The type of the data that the buffer stores.
	 * @return The buffer attached to the input port.
	 * @throws IllegalArgumentException If there does not exist an input port identified by {@code portName}.
	 */
	@SuppressWarnings("unchecked")
	protected <T> Buffer<T> getBuffer(String portName) {

		final Buffer<?> buffer = inputPorts.get(normalisePortName(portName));

		if (buffer == null) {
			throw new IllegalArgumentException("Unknown port: " + portName);
		}

		return (Buffer<T>) buffer;
	}

	/**
	 * Removes all this component's input/output ports and connections.
	 *
	 * Any future attempts to transmit data on this component should
	 * raise an {@code Exception} as this component is no longer in
	 * an accepting state.
	 */
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