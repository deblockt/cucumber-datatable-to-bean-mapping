package com.deblock.cucumber.datatable.mapper;

public interface MapperFactory {

    DatatableMapper build(Class<?> recordClass, TypeMetadataFactory typeMetadataFactory);
}
