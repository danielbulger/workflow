package com.danielbulger.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class GraphExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(GraphExecutor.class);

	private final Set<Node> nodes = Collections.synchronizedSet(new HashSet<>());

	private final ExecutorService worker;

	private final NodeMonitor monitor;

	public GraphExecutor(ExecutorService worker, NodeMonitor monitor) {
		this.worker = Objects.requireNonNull(worker);
		this.monitor = Objects.requireNonNull(monitor);
	}

	public void scheduleSelfStarting() {
		nodes.stream()
			.filter(Node::isSelfStarting)
			.forEach(this::schedule);
	}

	public void schedule(final Node node) {
		Objects.requireNonNull(node);

		// If the node isn't part of this graph
		// either due to an error or previously
		// removed, don't run it.
		if (!nodes.contains(node)) {
			LOG.warn("Attempted to remove invalid node {}", node);
			return;
		}

		monitor.onNodeDataReceive(node);

		worker.submit(new NodeTask(node, monitor));
	}

	public void addNode(final Node node) {
		nodes.add(Objects.requireNonNull(node));

		LOG.info("Added Node {}", node);
	}

	public void removeNode(final Node node) {
		Objects.requireNonNull(node);

		// Remove the input/outputs for the node.
		node.detach();

		// Remove the node from the graph
		nodes.remove(node);

		LOG.info("Removed Node {}", node);
	}
}
