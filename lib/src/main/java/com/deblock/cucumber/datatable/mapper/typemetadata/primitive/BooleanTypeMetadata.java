package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public class BooleanTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "boolean";
    }

    @Override
    public String sample() {
        return "true";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return "true".equals(value);
    }
}
