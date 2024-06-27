package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;

import java.lang.reflect.Type;

public interface MapperFactory {

    DatatableMapper build(Class<?> recordClass);

    DatatableMapper build(Column column, ColumnName name, Type type);
}
