package com.danielbulger.workflow;

public interface NetworkSerializer {

	Network deserialize() throws Exception;

	void serialise(Network network) throws Exception;
}
