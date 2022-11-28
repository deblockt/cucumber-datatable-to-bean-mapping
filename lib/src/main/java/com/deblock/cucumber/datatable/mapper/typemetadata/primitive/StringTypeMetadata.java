package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public class StringTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "string";
    }

    @Override
    public String sample() {
        return "string";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return value.trim();
    }
}
