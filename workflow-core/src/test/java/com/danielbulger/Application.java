package com.danielbulger;

import com.danielbulger.workflow.Edge;
import com.danielbulger.workflow.UnboundedBuffer;
import com.danielbulger.workflow.test.StringPrinter;
import com.danielbulger.workflow.test.StringProducer;

public class Application {

	public static void main(String[] args) throws Exception {
		StringProducer producer = new StringProducer();

		producer.addOutput(StringProducer.OUTPUT_STRING);

		StringPrinter printer = new StringPrinter();

		printer.addInput(StringPrinter.INPUT_STRING, new UnboundedBuffer<>());

		Edge edge = new Edge(printer, StringPrinter.INPUT_STRING);

		producer.connect(StringProducer.OUTPUT_STRING, edge);

		producer.execute();

		printer.execute();
	}
}
