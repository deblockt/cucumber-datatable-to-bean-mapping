package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.data.TypeMetadata;

public interface TypeMetadataFactory {
    TypeMetadata build(Class<?> aClass);
}
