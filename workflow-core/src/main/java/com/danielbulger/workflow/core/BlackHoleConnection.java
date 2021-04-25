package com.danielbulger.workflow.core;

/**
 * A {@code BlackHoleConnection} takes any input and <strong>DOES NOT</strong> pass it along.
 * <p>
 * This should be used for any {@link Port}s that do not require a {@link Connection}s.
 */
public class BlackHoleConnection implements Connection {

	@Override
	public void transfer(Object data) {

	}
}
