package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.math.BigDecimal;

public class BigDecimalTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "bigdecimal";
    }

    @Override
    public String sample() {
        return "10.15";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return new BigDecimal(value.replace(',', '.'));
    }
}
