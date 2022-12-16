package com.deblock.cucumber.datatable.mapper.typemetadata;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompositeTypeMetadataFactory implements TypeMetadataFactory {
    private final List<TypeMetadataFactory> factoryList;

    public CompositeTypeMetadataFactory(TypeMetadataFactory... factoryList) {
        this.factoryList = new ArrayList<>(Arrays.asList(factoryList));
    }

    @Override
    public TypeMetadata build(Type type) {
        return factoryList.stream()
                .map(factory -> factory.build(type))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can not find any converter for class " + type + ". You can define your own converter using @CustomDatatableFieldMapper"));
    }

    public void add(TypeMetadataFactory typeMetadataFactory) {
        this.factoryList.add(typeMetadataFactory);
    }
}
