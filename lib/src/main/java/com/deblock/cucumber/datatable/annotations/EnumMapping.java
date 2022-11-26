package com.deblock.cucumber.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * By default, Enum are converted by valueOf method
 *
 * If you want to use a custom property, you can use this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumMapping {

    /**
     * the field to use. Can be a public string field or a public method returning a string
     */
    String value();
}
