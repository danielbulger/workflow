package com.danielbulger.workflow;

import java.util.Collection;

public interface Buffer<T> {

	T head();

	T take();

	void insert(T value);

	Collection<T> drain(int count);

	int size();

	default boolean isEmpty() {
		return size() == 0;
	}
}
