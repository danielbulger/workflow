package com.danielbulger.workflow.core;

import java.util.Objects;

/**
 * A {@code PortConnection} is a logical connection between a single {@code OUTPUT} {@link Port}
 * and another {@code INPUT} {@link Port}.
 * <p>
 * This will transfer any data sent from the {@code OUTPUT} {@link Port} to the {@link Buffer} of the {@code INPUT} {@link Port}.
 */
public class PortConnection implements Connection {

	private final Component fromComponent;

	private final String fromPort;

	private final Component toComponent;

	private final String toPort;

	private final Class<?> type;

	/**
	 * Creates a new {@code PortConnection}.
	 *
	 * @param fromComponent The Component of the output buffer.
	 * @param fromPort      The port that the data will be transferred from.
	 * @param toComponent   The Component of the input buffer.
	 * @param toPort        The port that the data will be transferred to.
	 * @param type          The value type of the data.
	 * @throws NullPointerException     If any of the parameters are {@code null}.
	 * @throws IllegalArgumentException If {@code fromComponent} and {@code toComponent} are the same component.
	 */
	public PortConnection(
		Component fromComponent,
		String fromPort,
		Component toComponent,
		String toPort,
		Class<?> type
	) {
		if (fromComponent.equals(toComponent)) {
			throw new IllegalArgumentException("fromComponent must not be the same as the toComponent");
		}

		this.fromComponent = Objects.requireNonNull(fromComponent, "From component must not be null");
		this.fromPort = Objects.requireNonNull(fromPort, "From port must not be null");
		this.toComponent = Objects.requireNonNull(toComponent, "To component must not be null");
		this.toPort = Objects.requireNonNull(toPort, "To port must not be null");
		this.type = Objects.requireNonNull(type, "Type must not be null");
	}

	/**
	 * Transfers the data between the output and input ports of the components.
	 * <p>
	 * If the input component is no longer accepting data, this connection will
	 * disconnect itself from the {@code output} component.
	 *
	 * @param data The data that will be transferred.
	 * @throws IllegalArgumentException If the type of {@code data} is not accepted by this connection type.
	 */
	@Override
	public void transfer(Object data) {
		if (!type.isAssignableFrom(data.getClass())) {
			throw new IllegalArgumentException();
		}

		try {
			toComponent.receive(toPort, data);
		} catch (Exception exception) {
			fromComponent.removeConnection(fromPort, this);
		}
	}
}
