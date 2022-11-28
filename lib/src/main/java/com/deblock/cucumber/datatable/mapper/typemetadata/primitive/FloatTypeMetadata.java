package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public class FloatTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "float";
    }

    @Override
    public String sample() {
        return "10.10";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return Float.valueOf(value.replace(',', '.'));
    }
}
