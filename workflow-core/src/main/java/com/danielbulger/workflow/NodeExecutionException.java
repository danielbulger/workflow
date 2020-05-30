package com.danielbulger.workflow;

import java.util.concurrent.ExecutionException;

public class NodeExecutionException extends ExecutionException {

	public NodeExecutionException() {
	}

	public NodeExecutionException(String message) {
		super(message);
	}

	public NodeExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NodeExecutionException(Throwable cause) {
		super(cause);
	}
}
