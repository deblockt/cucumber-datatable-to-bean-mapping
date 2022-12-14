package com.deblock.cucumber.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allow to define a dataTable column.
 * The mapped name can be defined by value field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT, ElementType.PARAMETER})
public @interface Column {
    /**
     * the column name, you can specify multiple value to allow to use multiple column names
     */
    String[] value();

    String description() default "";

    boolean mandatory() default true;

    String defaultValue() default "";
}
