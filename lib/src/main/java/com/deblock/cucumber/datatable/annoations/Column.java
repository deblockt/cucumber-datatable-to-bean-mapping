package com.deblock.cucumber.datatable.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allow to define a dataTable column.
 * The mapped name can be defined by value field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String[] value() default {};

    String description() default "";

    boolean mandatory() default true;

    String defaultValue() default "";
}
