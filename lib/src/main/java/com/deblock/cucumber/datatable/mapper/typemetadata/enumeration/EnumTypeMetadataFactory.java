package com.deblock.cucumber.datatable.mapper.typemetadata.enumeration;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.Type;

public class EnumTypeMetadataFactory implements TypeMetadataFactory {
    @Override
    public TypeMetadata build(Type type) {
        if (type instanceof Class cType && Enum.class.isAssignableFrom(cType)) {
            return new EnumTypeMetadata(cType);
        }
        return null;
    }
}
