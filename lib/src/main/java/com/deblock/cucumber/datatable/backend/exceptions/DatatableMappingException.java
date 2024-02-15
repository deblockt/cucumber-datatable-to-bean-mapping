package com.deblock.cucumber.datatable.backend.exceptions;

public class DatatableMappingException extends RuntimeException {
    public DatatableMappingException(Class<?> dataTableClass, Throwable dataTableDoesNotMatch) {
        super("Could not transform datatable to type " + dataTableClass + "\n" + dataTableDoesNotMatch.getMessage());
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
