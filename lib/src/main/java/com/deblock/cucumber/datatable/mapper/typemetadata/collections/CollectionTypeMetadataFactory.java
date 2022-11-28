package com.deblock.cucumber.datatable.mapper.typemetadata.collections;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionTypeMetadataFactory implements TypeMetadataFactory {
    private final TypeMetadataFactory typeMetadataFactory;

    public CollectionTypeMetadataFactory(TypeMetadataFactory typeMetadataFactory) {
        this.typeMetadataFactory = typeMetadataFactory;
    }

    @Override
    public TypeMetadata build(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            final var rawType = parameterizedType.getRawType();
            if (Collection.class.equals(rawType) || List.class.equals(rawType)) {
                final var genericTypeMetadata = this.typeMetadataFactory.build(parameterizedType.getActualTypeArguments()[0]);
                return new CollectionLikeTypeMetadata(genericTypeMetadata, Collectors.toList(), "list");
            } else if (Set.class.equals(rawType)) {
                final var genericTypeMetadata = this.typeMetadataFactory.build(parameterizedType.getActualTypeArguments()[0]);
                return new CollectionLikeTypeMetadata(genericTypeMetadata, Collectors.toSet(), "set");
            }
        }
        return null;
    }
}
