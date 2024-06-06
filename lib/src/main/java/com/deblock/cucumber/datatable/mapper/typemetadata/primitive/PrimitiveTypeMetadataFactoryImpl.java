package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrimitiveTypeMetadataFactoryImpl implements TypeMetadataFactory {
    private final Map<Class<?>, TypeMetadata> primitiveTypeMetadata;

    public PrimitiveTypeMetadataFactoryImpl() {
        primitiveTypeMetadata = new HashMap<>();
        primitiveTypeMetadata.put(int.class, new NumericTypeMetadata("int", "10", Integer::parseInt));
        primitiveTypeMetadata.put(Integer.class, new NumericTypeMetadata("int", "10", Integer::parseInt));
        primitiveTypeMetadata.put(Long.class, new NumericTypeMetadata("long", "10", Long::parseLong));
        primitiveTypeMetadata.put(long.class, new NumericTypeMetadata("long", "10", Long::parseLong));
        primitiveTypeMetadata.put(Double.class, new NumericTypeMetadata("double", "10.10", Double::parseDouble));
        primitiveTypeMetadata.put(double.class, new NumericTypeMetadata("double", "10.10", Double::parseDouble));
        primitiveTypeMetadata.put(Float.class, new NumericTypeMetadata("float", "10.10", Float::parseFloat));
        primitiveTypeMetadata.put(float.class, new NumericTypeMetadata("float", "10.10", Float::parseFloat));
        primitiveTypeMetadata.put(short.class, new NumericTypeMetadata("short", "10", Short::parseShort));
        primitiveTypeMetadata.put(Short.class, new NumericTypeMetadata("short", "10", Short::parseShort));
        primitiveTypeMetadata.put(BigDecimal.class, new NumericTypeMetadata("bigDecimal", "10.10", BigDecimal::new));
        primitiveTypeMetadata.put(BigInteger.class, new NumericTypeMetadata("bigInteger", "10", BigInteger::new));
        primitiveTypeMetadata.put(Boolean.class, new BooleanTypeMetadata());
        primitiveTypeMetadata.put(boolean.class, new BooleanTypeMetadata());
        primitiveTypeMetadata.put(String.class, new StringTypeMetadata());
        primitiveTypeMetadata.put(UUID.class, new UUIDTypeMetadata());
    }

    @Override
    public TypeMetadata build(Type type) {
        return primitiveTypeMetadata.get(type);
    }
}
