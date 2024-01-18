package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnAnnotatedObjectDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.RecordDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.SimpleColumnDatatableMapper;

import java.lang.reflect.Type;

public class GenericMapperFactory implements MapperFactory {
    private final TypeMetadataFactory typeMetadataFactory;

    public GenericMapperFactory(TypeMetadataFactory typeMetadataFactory) {
        this.typeMetadataFactory = typeMetadataFactory;
    }

    @Override
    public DatatableMapper build(Class<?> clazz) {
        if (clazz.isRecord()) {
            return new RecordDatatableMapper(clazz, this);
        } else {
            return new BeanDatatableMapper(clazz, this);
        }
    }

    @Override
    public DatatableMapper build(Column column, String name, Type type) {
        if (type instanceof Class<?> clazz && clazz.isAnnotationPresent(DataTableWithHeader.class)) {
            return new ColumnAnnotatedObjectDatatableMapper(column, this.build(clazz));
        }

        return new SimpleColumnDatatableMapper(
            column,
            name,
            type,
            typeMetadataFactory
        );
    }
}
