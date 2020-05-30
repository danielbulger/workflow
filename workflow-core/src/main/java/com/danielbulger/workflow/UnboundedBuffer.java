package com.danielbulger.workflow;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UnboundedBuffer<T> implements Buffer<T> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	@Nullable
	@Contract(pure = true)
	@Override
	public T head() {
		return queue.peek();
	}

	@NotNull
	@Contract(mutates = "this")
	@Override
	public T drop() {

		final T head = queue.poll();

		if (head == null) {
			throw new IllegalStateException();
		}

		return head;
	}

	@Contract(pure = true)
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Contract(pure = true)
	@Override
	public int size() {
		return queue.size();
	}

	@NotNull
	@Contract(mutates = "this")
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

	@Contract(mutates = "this")
	@Override
	public void insert(@NotNull T element) {
		queue.add(Objects.requireNonNull(element));
	}
}
