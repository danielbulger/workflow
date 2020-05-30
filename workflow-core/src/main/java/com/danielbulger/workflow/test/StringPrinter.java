package com.danielbulger.workflow.test;

import com.danielbulger.workflow.Buffer;
import com.danielbulger.workflow.Node;
import com.danielbulger.workflow.NodeExecutionException;
import com.danielbulger.workflow.Property;

public class StringPrinter extends Node {

	public static final Property INPUT_STRING = new Property(
		String.class,
		"string",
		""
	);

	@Override
	public void execute() throws NodeExecutionException {

		final Buffer<String> buffer = super.getInput(INPUT_STRING);

		if(buffer.isEmpty()) {
			return;
		}

		final String str = buffer.drop();

		System.out.println(str);
	}
}
