package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;
import com.deblock.cucumber.datatable.mapper.datatable.BaseObjectDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnAnnotatedObjectDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.RecordDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.SimpleColumnDatatableMapper;
import com.deblock.cucumber.datatable.mapper.name.DataTableColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.exceptions.NoConverterFound;

import java.lang.reflect.Type;

public class GenericMapperFactory implements MapperFactory {
    private final TypeMetadataFactory typeMetadataFactory;
    private final DataTableColumnNameBuilder columnNameBuilder;

    public GenericMapperFactory(TypeMetadataFactory typeMetadataFactory, DataTableColumnNameBuilder columnNameBuilder) {
        this.typeMetadataFactory = typeMetadataFactory;
        this.columnNameBuilder = columnNameBuilder;
    }

    @Override
    public DatatableMapper build(Class<?> clazz) {
        return getBaseObjectDatatableMapper(clazz, new ColumnName());
    }

    @Override
    public DatatableMapper build(Column column, ColumnName columnName, Type type) {
        try {
            typeMetadataFactory.build(type);
            return new SimpleColumnDatatableMapper(
                    column,
                    columnName,
                    type,
                    typeMetadataFactory
            );
        } catch (NoConverterFound ex) {
            if (type instanceof Class<?> clazz && clazz.isAnnotationPresent(DataTableWithHeader.class)) {
                return new ColumnAnnotatedObjectDatatableMapper(column, this.getBaseObjectDatatableMapper(clazz, columnName));
            }
            throw ex;
        }
    }

    private BaseObjectDatatableMapper<? extends DatatableMapper> getBaseObjectDatatableMapper(Class<?> clazz, ColumnName name) {
        if (clazz.isRecord()) {
            return new RecordDatatableMapper(clazz, this, name, this.columnNameBuilder);
        } else {
            return new BeanDatatableMapper(clazz, this, name, this.columnNameBuilder);
        }
    }
}
