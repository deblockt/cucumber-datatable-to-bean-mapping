package com.deblock.cucumber.datatable.mapper.beans.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;

public record CustomBean(String value) {

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean")
    public static CustomBean mapFromString(String value) {
        return new CustomBean(value);
    }

    public static CustomBean mapFromNonString(Integer value) {
        return null;
    }

    public CustomBean nonStaticMapperForTest(String value) {
        return null;
    }

    public static CustomBean mapperWithoutParameter() {
        return null;
    }

    public static CustomBean mapperWithTwoParametersParameter(String value1, String value2) {
        return null;
    }
}
