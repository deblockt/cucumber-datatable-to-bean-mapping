package com.deblock.cucumber.datatable.mapper.beans.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;

public record CustomBean2(String value) {

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean2")
    public static CustomBean2 mapperThrowingException(String value) {
        throw new IllegalArgumentException("error " + value + " is not supported");
    }
}
