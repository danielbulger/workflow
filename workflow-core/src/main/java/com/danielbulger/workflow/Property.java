package com.danielbulger.workflow;

import java.util.Objects;

public class Property {

	private final Class<?> type;

	private final String name;

	private final String description;

	public Property(
		Class<?> type,
		String name,
		String description
	) {
		this.type = Objects.requireNonNull(type);
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
	}

	public boolean isAllowedType(Class<?> clasz) {
		return clasz == type || type.isAssignableFrom(clasz);
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
		return "Property{" +
			"name='" + name + '\'' +
			'}';
	}
}
