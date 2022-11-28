package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.math.BigInteger;

public class BigIntegerTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "biginteger";
    }

    @Override
    public String sample() {
        return "10";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return new BigInteger(value);
    }
}
