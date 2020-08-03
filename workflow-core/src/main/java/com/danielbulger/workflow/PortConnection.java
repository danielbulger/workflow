package com.danielbulger.workflow;

import java.util.Objects;

public class PortConnection implements Connection {

	private final Component fromComponent;

	private final String fromPort;

	private final Component toComponent;

	private final String toPort;

	private final Class<?> type;

	public PortConnection(
		Component fromComponent,
		String fromPort,
		Component toComponent,
		String toPort,
		Class<?> type
	) {
		this.fromComponent = Objects.requireNonNull(fromComponent, "From component must not be null");
		this.fromPort = Objects.requireNonNull(fromPort, "From port must not be null");
		this.toComponent = Objects.requireNonNull(toComponent, "To component must not be null");
		this.toPort = Objects.requireNonNull(toPort, "To port must not be null");
		this.type = Objects.requireNonNull(type, "Type must not be null");
	}

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
