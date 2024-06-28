package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;

import java.lang.reflect.Type;

public interface MapperFactory {

    DatatableMapper build(Class<?> recordClass);

    DatatableMapper build(FieldResolver.FieldInfo column, ColumnName name, Type type);
}
