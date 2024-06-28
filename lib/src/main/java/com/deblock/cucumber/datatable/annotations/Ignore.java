package com.deblock.cucumber.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation signals a class/field that should not be mapped.
 * This annotation can be used when property cucumber.datatable.mapper.field-resolver-class is equals to
 * com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.ImplicitFieldResolver
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.RECORD_COMPONENT, ElementType.PARAMETER})
public @interface Ignore {
}
