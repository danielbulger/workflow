package com.danielbulger.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkMonitor {

	private static final Logger LOG = LoggerFactory.getLogger(NetworkMonitor.class);

	public void onNodeStart(Component node) {
		LOG.debug("Node {} started processing...", node);
	}

	public void onNodeFailure(Component node, Throwable t) {
		LOG.debug("Node {} failed processing with error {}", node, t);
	}

	public void onNodeSuccess(Component node) {
		LOG.debug("Node {} successfully executed", node);
	}

	public void onNodeDataReceive(Component node) {
		LOG.debug("Node {} has received data", node);
	}
}
