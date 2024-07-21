package com.deblock.cucumber.datatable.backend.exceptions;

public class DatatableMappingException extends RuntimeException {
    public DatatableMappingException(Class<?> dataTableClass, Throwable cause) {
        super("Could not transform datatable to type " + dataTableClass + "\n" + cause.getMessage());
        this.setStackTrace(cause.getStackTrace());
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
