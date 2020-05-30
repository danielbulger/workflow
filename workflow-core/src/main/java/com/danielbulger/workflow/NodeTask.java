package com.danielbulger.workflow;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class NodeTask implements Runnable {

	private final Node node;

	private final NodeMonitor monitor;

	public NodeTask(@NotNull Node node, @NotNull NodeMonitor monitor) {
		this.node = Objects.requireNonNull(node);
		this.monitor = Objects.requireNonNull(monitor);
	}

	@Override
	public void run() {
		try {

			monitor.onNodeStart(node);

			node.execute();

			monitor.onNodeSuccess(node);

		} catch (Throwable throwable) {
			monitor.onNodeFailure(node, throwable);
		}
	}
}
