package com.deblock.cucumber.datatable.mapper.typemetadata.map;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import io.cucumber.messages.ndjson.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.messages.ndjson.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.messages.ndjson.internal.com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;

public class MapTypeMetadata implements TypeMetadata {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
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
            return OBJECT_MAPPER.readValue(value, new TypeReference<>() {
                @Override
                public Type getType() {
                    return MapTypeMetadata.this.parameterizedType;
                }
            });
        } catch (JsonProcessingException e) {
            throw new ConversionError(e.getMessage(), e);
        }
    }
}
