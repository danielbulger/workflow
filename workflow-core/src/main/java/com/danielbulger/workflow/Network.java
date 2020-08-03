package com.danielbulger.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Represents a Graph of {@link Component}s.
 */
public class Network {

	private static final Logger LOG = LoggerFactory.getLogger(Network.class);

	private final ExecutorService worker;

	private final NetworkMonitor monitor;

	private final Set<Component> nodes = Collections.synchronizedSet(new HashSet<>());

	/**
	 * Creates a new {@code Network}.
	 *
	 * @param worker  The Executor that will run the {@link Component} tasks.
	 * @param monitor The {@link NetworkMonitor} that will monitor the {@link Component} tasks.
	 */
	public Network(ExecutorService worker, NetworkMonitor monitor) {
		this.worker = Objects.requireNonNull(worker);

		this.monitor = Objects.requireNonNull(monitor);
	}

	/**
	 * {@link #schedule(Component)} any self starting {@code Components} in this {@code Network}.
	 *
	 * @see Component#isSelfStarting()
	 */
	public void scheduleSelfStarting() {
		nodes.stream()
			.filter(Component::isSelfStarting)
			.forEach(this::schedule);
	}

	/**
	 * Schedules the {@code node} for execution.
	 * <p>
	 * NOTE: There is no guarantee that the {@code node} will be executed.
	 * If the {@link ExecutorService} is shutdown immediately instead of gracefully, this
	 * {@code node} will never rune.
	 *
	 * @param node The {@code node} that will be executed.
	 * @see NodeTask
	 */
	public void schedule(Component node) {
		if (!nodes.contains(node)) {
			LOG.warn("Attempted to run node {} that is not part of this network", node);
			return;
		}

		monitor.onNodeDataReceive(node);

		worker.submit(new NodeTask(node, monitor));
	}

	/**
	 * Adds the {@code node} to this network.
	 *
	 * @param node The {@code node} that will be added to the network.
	 * @throws IllegalArgumentException If the {@code node} is {@code null}
	 */
	public void addNode(Component node) {
		if (node == null) {
			throw new IllegalArgumentException();
		}

		nodes.add(node);
	}

	/**
	 * Removes and detaches the {@code node} from this network.
	 *
	 * @param node The {@code node} to remove.
	 * @throws IllegalArgumentException If the {@code node} is {@code null}
	 * @see Component#detach()
	 */
	public void removeNode(Component node) {
		if (node == null) {
			throw new IllegalArgumentException();
		}
		nodes.remove(node);

		node.detach();
	}
}
