package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.lang.reflect.Type;

public interface TypeMetadataFactory {
    TypeMetadata build(Type type);
}
