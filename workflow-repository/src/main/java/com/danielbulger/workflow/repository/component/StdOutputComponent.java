package com.danielbulger.workflow.repository.component;

import com.danielbulger.workflow.core.*;

@Port(
	name = "message",
	valueType = String.class,
	type = PortType.INPUT
)
public class StdOutputComponent extends Component {

	public StdOutputComponent(Network network) {
		super(network);
	}

	@Override
	public void execute() {
		final Buffer<String> msgBuffer = super.getBuffer("message");

		if (msgBuffer.isEmpty()) {
			return;
		}

		final String msg = msgBuffer.take();

		System.out.println(msg);
	}
}
