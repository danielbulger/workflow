package com.danielbulger.workflow;

/**
 * Represents a logical I/O connection for the {@code output} {@link Port}s.
 * <p>
 * This is responsible for transferring data from an output port to some accepting input.
 */
public interface Connection {

	/**
	 * Transfers the {@code data} through this connection.
	 *
	 * @param data The data that will be transferred.
	 */
	void transfer(Object data);

}
