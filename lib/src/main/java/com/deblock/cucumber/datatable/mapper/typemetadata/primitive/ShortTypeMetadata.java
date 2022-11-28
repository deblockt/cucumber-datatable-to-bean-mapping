package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public class ShortTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "short";
    }

    @Override
    public String sample() {
        return "10";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return Short.valueOf(value);
    }
}
