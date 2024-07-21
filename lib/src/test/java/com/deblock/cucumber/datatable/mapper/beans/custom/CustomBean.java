package com.deblock.cucumber.datatable.mapper.beans.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;

public record CustomBean(String value) {

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean")
    public static CustomBean mapFromString(String value) {
        return new CustomBean(value);
    }

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean")
    public static CustomBean mapFromNonString(Integer value) {
        return null;
    }

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean")
    public CustomBean nonStaticMapperForTest(String value) {
        return new CustomBean(value);
    }

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean")
    public static CustomBean mapperWithoutParameter() {
        return null;
    }

    @CustomDatatableFieldMapper(sample = "aString", typeDescription = "CustomBean")
    public static CustomBean mapperWithTwoParametersParameter(String value1, String value2) {
        return null;
    }
}
