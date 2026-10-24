package com.deblock.cucumber.datatable.mapper;

/**
 * Thrown when a bean, a record or an enum value can not be created from a datatable.
 * The cause is the exception thrown by the user code (constructor, setter) when there is one.
 */
public class ObjectCreationException extends RuntimeException {

    public ObjectCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
