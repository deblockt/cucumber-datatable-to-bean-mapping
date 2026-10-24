package com.deblock.cucumber.datatable.mapper.typemetadata.map;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MapTypeMetadata implements TypeMetadata {
    private static final Gson GSON = new Gson();
    private final Type parameterizedType;

    public MapTypeMetadata(Type parameterizedType) {
        this.parameterizedType = parameterizedType;
    }

    @Override
    public String typeDescription() {
        return "json object";
    }

    @Override
    public String sample() {
        return "{\"foo\": \"bar\"}";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        try {
            return GSON.fromJson(value, this.parameterizedType);
        } catch (JsonParseException e) {
            // gson wraps the parsing error, the cause holds the readable message
            final var message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            throw new ConversionError(message, e);
        }
    }
}
