package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CustomTypeMetadataFactory implements TypeMetadataFactory {
    public static final CustomTypeMetadataFactory INSTANCE = new CustomTypeMetadataFactory();
    private final Map<Class<?>, TypeMetadata> customTypes = new HashMap<>();

    private CustomTypeMetadataFactory() {

    }

    @Override
    public TypeMetadata build(Type type) {
        return customTypes.get(type);
    }

    public static void addCustomType(Class<?> clazz, TypeMetadata typeMetadata) {
        INSTANCE.customTypes.put(clazz, typeMetadata);
    }
}
