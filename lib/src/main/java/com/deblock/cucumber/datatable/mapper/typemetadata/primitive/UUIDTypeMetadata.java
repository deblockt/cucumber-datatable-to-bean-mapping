package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.util.UUID;

public class UUIDTypeMetadata implements TypeMetadata {
    @Override
    public String typeDescription() {
        return "uuid";
    }

    @Override
    public String sample() {
        return "dd5ae1f7-313a-4a58-b8f5-61b97283acc2";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new TypeMetadata.ConversionError("invalid format for uuid");
        }
    }
}
