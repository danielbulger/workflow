package com.danielbulger.workflow.core;

public interface NetworkSerializer {

	Network deserialize() throws Exception;

	void serialise(Network network) throws Exception;
}
