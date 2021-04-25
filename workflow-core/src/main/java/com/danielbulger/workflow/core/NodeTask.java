package com.danielbulger.workflow.core;

import java.util.Objects;

public class NodeTask implements Runnable {

	private final Component node;

	private final NetworkMonitor monitor;

	public NodeTask(Component node, NetworkMonitor monitor) {
		this.node = Objects.requireNonNull(node);
		this.monitor = Objects.requireNonNull(monitor);
	}

	@Override
	public void run() {
		monitor.onNodeStart(node);

		try {
			node.doExecute();

			monitor.onNodeSuccess(node);
		} catch (Throwable t) {
			monitor.onNodeFailure(node, t);
		}
	}
}
