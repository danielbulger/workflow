package com.danielbulger.workflow;

public class PortConnection implements Connection {

	private final Component fromComponent;

	private final String fromPort;

	private final Component toComponent;

	private final String toPort;

	private final Class<?> type;

	public PortConnection(Component fromComponent, String fromPort, Component toComponent, String toPort, Class<?> type) {
		this.fromComponent = fromComponent;
		this.fromPort = fromPort;
		this.toComponent = toComponent;
		this.toPort = toPort;
		this.type = type;
	}

	@Override
	public void transfer(Object value) {
		if (!type.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException();
		}

		try {
			toComponent.receive(toPort, value);
		} catch (Exception exception) {
			fromComponent.removeConnection(fromPort, this);
		}
	}
}
