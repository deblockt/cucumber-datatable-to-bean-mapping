package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveTypeMetadataFactoryImpl implements TypeMetadataFactory {
    private final Map<Class<?>, TypeMetadata> primitiveTypeMetadata;

    public PrimitiveTypeMetadataFactoryImpl() {
        primitiveTypeMetadata = new HashMap<>();
        primitiveTypeMetadata.put(int.class, new IntTypeMetadata());
        primitiveTypeMetadata.put(Integer.class, new IntTypeMetadata());
        primitiveTypeMetadata.put(Long.class, new LongTypeMetadata());
        primitiveTypeMetadata.put(long.class, new LongTypeMetadata());
        primitiveTypeMetadata.put(Double.class, new DoubleTypeMetadata());
        primitiveTypeMetadata.put(double.class, new DoubleTypeMetadata());
        primitiveTypeMetadata.put(Float.class, new FloatTypeMetadata());
        primitiveTypeMetadata.put(float.class, new FloatTypeMetadata());
        primitiveTypeMetadata.put(String.class, new StringTypeMetadata());
        primitiveTypeMetadata.put(short.class, new ShortTypeMetadata());
        primitiveTypeMetadata.put(Short.class, new ShortTypeMetadata());
        primitiveTypeMetadata.put(Boolean.class, new BooleanTypeMetadata());
        primitiveTypeMetadata.put(boolean.class, new BooleanTypeMetadata());
    }

    @Override
    public TypeMetadata build(Type type) {
        return primitiveTypeMetadata.get(type);
    }
}
