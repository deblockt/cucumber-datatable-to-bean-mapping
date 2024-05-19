package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.lang.reflect.Type;

public interface MapperFactory {

    DatatableMapper build(Class<?> recordClass);

    DatatableMapper build(Column column, ColumnNameBuilder name, Type type);

    DatatableMapper build(ColumnNameBuilder name, Type type);

}