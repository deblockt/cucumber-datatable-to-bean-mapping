package com.deblock.cucumber.datatable.mapper.typemetadata.collections;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.util.Arrays;
import java.util.stream.Collector;

public class CollectionLikeTypeMetadata implements TypeMetadata {

    private final TypeMetadata genericTypeMetadata;
    private final Collector<Object,?, ?> collector;
    private final String collectionName;

    public <T> CollectionLikeTypeMetadata(
            TypeMetadata genericTypeMetadata,
            Collector<Object,?, ?> collector,
            String collectionName
    ) {
        this.genericTypeMetadata = genericTypeMetadata;
        this.collector = collector;
        this.collectionName = collectionName;
    }

    @Override
    public String typeDescription() {
        return collectionName + "<" + genericTypeMetadata.typeDescription() + ">";
    }

    @Override
    public String sample() {
        return genericTypeMetadata.sample() + "," + genericTypeMetadata.sample();
    }

    @Override
    public Object convert(String value) throws ConversionError {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .map(genericTypeMetadata::convert)
                .collect(this.collector);
    }
}
