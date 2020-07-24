package com.danielbulger.workflow;


import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UnboundedBuffer<T> implements Buffer<T> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	@Override
	public T head() {
		return queue.peek();
	}

	@Override
	public T drop() {

		final T head = queue.poll();

		if (head == null) {
			throw new IllegalStateException();
		}

		return head;
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T[] drain(int num) {

		if (num <= 0) {
			throw new IllegalArgumentException();
		}

		if (size() < num) {
			throw new IllegalStateException();
		}

		final Object[] elements = new Object[num];

		for (int i = 0; i < num; ++i) {
			elements[i] = queue.poll();
		}

		return (T[]) elements;
	}

	@Override
	public void insert(T element) {
		queue.add(Objects.requireNonNull(element));
	}
}
