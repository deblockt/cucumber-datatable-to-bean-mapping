package com.deblock.cucumber.datatable.backend.exceptions;

import com.deblock.cucumber.datatable.validator.DataTableDoesNotMatch;

public class DatatableMappingException extends RuntimeException {
    public DatatableMappingException(Class<?> dataTableClass, DataTableDoesNotMatch dataTableDoesNotMatch) {
        super("Could not transform datatable to type " + dataTableClass + "\n" + dataTableDoesNotMatch.getMessage());
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
