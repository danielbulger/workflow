package com.danielbulger.workflow;

public interface Buffer<T> {

	T head();

	T drop();

	default boolean isEmpty() {
		return size() == 0;
	}

	int size();

	T[] drain(int num);

	void insert(T element);
}
