package com.danielbulger.workflow.repository.buffer;

import com.danielbulger.workflow.Buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UnboundedBuffer<T> implements Buffer<T> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	@Override
	public T head() {
		return queue.peek();
	}

	@Override
	public T take() {

		final T head = queue.poll();

		if (head == null) {
			throw new NoSuchElementException();
		}

		return head;
	}

	@Override
	public void insert(T value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}

		queue.add(value);
	}

	@Override
	public Collection<T> drain(int count) {
		if (queue.size() < count) {
			throw new NoSuchElementException();
		}

		final Collection<T> elements = new ArrayList<>(count);

		for (int i = 0; i < count; ++i) {
			elements.add(queue.poll());
		}

		return elements;
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
