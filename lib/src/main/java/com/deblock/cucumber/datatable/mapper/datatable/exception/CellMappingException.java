package com.deblock.cucumber.datatable.mapper.datatable.exception;

public class CellMappingException extends RuntimeException {
    public CellMappingException(String headerName, Object value, String expectedType, Exception e) {
        super("Unable to convert value \"%s\" for column \"%s\" of type %s: %s".formatted(value, headerName, expectedType, e.getMessage()), e);
        this.setStackTrace(e.getStackTrace());
    }
}
