package com.danielbulger.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitor {

	private static final Logger LOG = LoggerFactory.getLogger(NodeMonitor.class);

	public void onNodeStart(Node node) {
		LOG.info("Node started processing: {}", node);
	}

	public void onNodeFailure(Node node, Throwable t) {
		LOG.error("Node failed during processing: " + node, t);
	}

	public void onNodeSuccess(Node node) {
		LOG.info("Node successfully processed: {}", node);
	}

	public void onNodeDataReceive(Node node) {
		LOG.info("Node has received data: {}", node);
	}
}
