package com.danielbulger.workflow.core;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Port.List.class)
public @interface Port {

	String name();

	String description() default "";

	Class<?> valueType();

	PortType type();

	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface List {

		Port[] value();
	}
}
