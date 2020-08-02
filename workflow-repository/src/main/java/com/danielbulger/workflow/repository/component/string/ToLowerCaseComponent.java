package com.danielbulger.workflow.repository.component.string;

import com.danielbulger.workflow.*;

@Port(
	name = "input",
	valueType = String.class,
	type = PortType.INPUT
)

@Port(
	name = "output",
	valueType = String.class,
	type = PortType.OUTPUT
)
public class ToLowerCaseComponent extends Component {

	public ToLowerCaseComponent(Network network) {
		super(network);
	}

	@Override
	public void execute() {
		final Buffer<String> buffer = super.getBuffer("input");

		if (buffer.isEmpty()) {
			return;
		}

		final String input = buffer.take();

		super.send("output", input.toLowerCase());
	}
}
