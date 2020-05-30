package com.danielbulger.workflow.test;

import com.danielbulger.workflow.Node;
import com.danielbulger.workflow.NodeExecutionException;
import com.danielbulger.workflow.Property;

public class StringProducer extends Node {

	public static final Property OUTPUT_STRING = new Property(
		String.class,
		"String",
		""
	);

	@Override
	public void execute() throws NodeExecutionException {
		super.send(OUTPUT_STRING, "Hello, World");
	}
}
