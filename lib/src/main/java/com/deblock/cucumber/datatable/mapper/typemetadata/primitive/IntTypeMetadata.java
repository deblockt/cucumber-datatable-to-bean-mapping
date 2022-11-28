package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public class IntTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "int";
    }

    @Override
    public String sample() {
        return "10";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return Integer.parseInt(value);
    }
}
