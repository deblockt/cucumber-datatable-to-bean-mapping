package com.deblock.cucumber.datatable.mapper.typemetadata.map;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapTypeMetadataFactory implements TypeMetadataFactory {
    @Override
    public TypeMetadata build(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            final var rawType = parameterizedType.getRawType();
            if (Map.class.equals(rawType)) {
                return new MapTypeMetadata(parameterizedType);
            }
        }
        return null;
    }
}
