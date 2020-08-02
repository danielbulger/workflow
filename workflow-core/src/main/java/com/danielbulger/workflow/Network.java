package com.danielbulger.workflow;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Network {

	private final ExecutorService worker;

	private final NetworkMonitor monitor;

	private final Set<Component> nodes = Collections.synchronizedSet(new HashSet<>());

	public Network(ExecutorService worker, NetworkMonitor monitor) {
		this.worker = Objects.requireNonNull(worker);

		this.monitor = Objects.requireNonNull(monitor);
	}

	public void schedule(Component node) {
		if (!nodes.contains(node)) {
			return;
		}

		monitor.onNodeDataReceive(node);

		worker.submit(new NodeTask(node, monitor));
	}

	public void addNode(Component node) {
		if (!nodes.add(node)) {

		}
	}

	public void removeNode(Component node) {
		nodes.remove(node);

		node.detach();
	}
}
