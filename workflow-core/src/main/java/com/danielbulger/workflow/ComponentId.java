package com.danielbulger.workflow;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class ComponentId implements Comparable<ComponentId> {

	private static final AtomicLong counter = new AtomicLong(0);

	/**
	 * Gets the next generated ComponentId.
	 *
	 * Note: This makes no guarantees that the id is not already in use.
	 *
	 * @return The newly generated ComponentId.
	 */
	public static ComponentId next() {
		return new ComponentId(counter.getAndIncrement());
	}

	private final long id;

	private ComponentId(long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		return id == ((ComponentId) o).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "ComponentId{" +
			"id=" + id +
			'}';
	}

	@Override
	public int compareTo(ComponentId o) {
		return Long.compare(id, o.id);
	}
}
