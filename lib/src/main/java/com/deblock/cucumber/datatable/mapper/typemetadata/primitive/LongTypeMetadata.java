package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public class LongTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "long";
    }

    @Override
    public String sample() {
        return "10";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return Long.parseLong(value);
    }
}
