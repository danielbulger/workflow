package com.danielbulger.workflow;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Buffer<T> {

	@Nullable
	@Contract(pure = true)
	T head();

	@NotNull
	T drop();

	@Contract(pure = true)
	default boolean isEmpty() {
		return size() == 0;
	}

	@Contract(pure = true)
	int size();

	@NotNull
	T[] drain(int num);

	void insert(@NotNull T element);
}
