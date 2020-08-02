package com.danielbulger.workflow.example;

import com.danielbulger.workflow.*;
import com.danielbulger.workflow.repository.buffer.UnboundedBuffer;
import com.danielbulger.workflow.repository.component.HttpComponent;
import com.danielbulger.workflow.repository.component.StdOutputComponent;
import com.danielbulger.workflow.repository.component.string.ToUpperCaseComponent;

import java.util.concurrent.Executors;

public class Application {

	public static void main(String[] args) throws Exception {

		final Network network = new Network(
			Executors.newFixedThreadPool(2),
			new NetworkMonitor()
		);

		final Component httpComponent = new HttpComponent(network);

		final Component printComponent = new StdOutputComponent(network);

		final Component toUpperComponent = new ToUpperCaseComponent(network);

		httpComponent.addInputPort("url", new UnboundedBuffer<>());

		httpComponent.addOutputConnection("error", new BlackHoleConnection());

		printComponent.addInputPort("message", new UnboundedBuffer<>());

		toUpperComponent.addInputPort("input", new UnboundedBuffer<>());

		httpComponent.addOutputConnection("output", new PortConnection(
			httpComponent,
			"output",
			printComponent,
			"message",
			String.class
		));

		httpComponent.addOutputConnection("output", new PortConnection(
			httpComponent,
			"output",
			toUpperComponent,
			"input",
			String.class
		));

		toUpperComponent.addOutputConnection("output", new PortConnection(
			toUpperComponent,
			"output",
			printComponent,
			"message",
			String.class
		));

		network.addNode(httpComponent);

		network.addNode(printComponent);

		network.addNode(toUpperComponent);

		httpComponent.receive("url", "https://www.google.com");
	}
}
