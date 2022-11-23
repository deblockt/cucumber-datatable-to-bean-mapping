package com.deblock.cucumber.datatable.validator;

public class DataTableDoesNotMatch extends RuntimeException {
    public DataTableDoesNotMatch(String reason) {
        super(reason);
    }
}