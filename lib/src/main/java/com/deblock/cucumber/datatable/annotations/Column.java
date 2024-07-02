package com.deblock.cucumber.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allow to define a dataTable column.
 * The column name can be defined by `value` field.
 * If value is not set, the column name is used to build the column name depending on the cucumber.datatable.mapper.name-builder-class property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT, ElementType.PARAMETER})
public @interface Column {
    /**
     * The column name, you can specify multiple value to allow to use multiple column names.
     * If not specified the name will be the field name.
     */
    String[] value() default {};

    String description() default "";

    boolean mandatory() default true;

    String defaultValue() default "";
}
