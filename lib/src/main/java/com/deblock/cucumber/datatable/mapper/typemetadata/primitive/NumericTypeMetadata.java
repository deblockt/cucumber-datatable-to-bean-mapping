package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.util.function.Function;

public class NumericTypeMetadata implements TypeMetadata {
    private final String typeDescription;
    private final String sample;
    private final Function<String, Number> mapper;

    public NumericTypeMetadata(String typeDescription, String sample, Function<String, Number> mapper) {
        this.typeDescription = typeDescription;
        this.sample = sample;
        this.mapper = mapper;
    }

    @Override
    public String typeDescription() {
        return this.typeDescription;
    }

    @Override
    public String sample() {
        return this.sample;
    }

    @Override
    public Object convert(String value) throws ConversionError {
        try {
            return this.mapper.apply(value.replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new ConversionError("\"%s\" is a invalid format for %s".formatted(value, this.typeDescription()));
        }
    }
}
