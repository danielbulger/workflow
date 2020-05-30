package com.danielbulger.workflow;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Property {

	private final Class<?> type;

	private final String name;

	private final String description;

	public Property(
		@NotNull Class<?> type,
		@NotNull String name,
		@NotNull String description
	) {
		this.type = Objects.requireNonNull(type);
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
	}

	public boolean isAllowedType(Class<?> clasz) {
		return clasz.isAssignableFrom(getType());
	}

	public Class<?> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Property port = (Property) o;
		return name.equals(port.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Port{" +
			"name='" + name + '\'' +
			'}';
	}
}
