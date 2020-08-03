package com.danielbulger.workflow;

import java.util.Collection;
import java.util.NoSuchElementException;

public interface Buffer<T> {

	/**
	 * Retrieves, but does not remove, the head of this buffer,
	 * or returns {@code null} if this buffer is empty.
	 *
	 * @return the head of this buffer, or {@code null} if this buffer is empty
	 */
	T head();

	/**
	 * Retrieves and removes the head of this buffer.  This method differs
	 * from {@link #head()} only in that it throws an exception if
	 * this buffer is empty.
	 *
	 * @return the head of this buffer
	 * @throws NoSuchElementException if this buffer is empty
	 */
	T take();

	/**
	 * Inserts the specified element into this buffer if it is possible to do so
	 * immediately without violating capacity restrictions, throwing an {@code IllegalStateException}
	 * if no space is currently available.
	 *
	 * @param value the element to add
	 * @throws IllegalStateException if the element cannot be added at this
	 *                               time due to capacity restrictions
	 */
	void insert(T value);

	/**
	 * Retrieves and removes {@code count} elements of this buffer if it is possible to do so,
	 * throwing an {@code NoSuchElementException} if there are not enough elements within the buffer
	 * to fulfill the request.
	 *
	 * @param count The number of elements to drain from this buffer.
	 * @return The Collection that contains the drained elements from this buffer
	 */
	Collection<T> drain(int count);

	/**
	 * Returns the number of elements in this buffer.
	 *
	 * @return the number of elements in this buffer
	 */
	int size();

	/**
	 * Returns {@code true} if this buffer contains no elements.
	 *
	 * @return {@code true} if this buffer contains no elements
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
}
