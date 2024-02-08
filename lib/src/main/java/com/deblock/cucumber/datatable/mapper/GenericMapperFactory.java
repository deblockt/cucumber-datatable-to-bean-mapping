package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;
import com.deblock.cucumber.datatable.mapper.datatable.BaseObjectDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnAnnotatedObjectDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.RecordDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.SimpleColumnDatatableMapper;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.lang.reflect.Type;

public class GenericMapperFactory implements MapperFactory {
    private final TypeMetadataFactory typeMetadataFactory;

    public GenericMapperFactory(TypeMetadataFactory typeMetadataFactory) {
        this.typeMetadataFactory = typeMetadataFactory;
    }

    @Override
    public DatatableMapper build(Class<?> clazz) {
        return getBaseObjectDatatableMapper(clazz, null);
    }

    @Override
    public DatatableMapper build(Column column, ColumnNameBuilder nameBuilder, Type type) {
        if (type instanceof Class<?> clazz && clazz.isAnnotationPresent(DataTableWithHeader.class)) {
            return new ColumnAnnotatedObjectDatatableMapper(column, this.getBaseObjectDatatableMapper(clazz, nameBuilder));
        }

        return new SimpleColumnDatatableMapper(
            column,
            nameBuilder,
            type,
            typeMetadataFactory
        );
    }

    private BaseObjectDatatableMapper<? extends DatatableMapper> getBaseObjectDatatableMapper(Class<?> clazz, ColumnNameBuilder nameBuilder) {
        if (clazz.isRecord()) {
            return new RecordDatatableMapper(clazz, this, nameBuilder);
        } else {
            return new BeanDatatableMapper(clazz, this, nameBuilder);
        }
    }
}
