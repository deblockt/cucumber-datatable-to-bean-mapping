package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Optional;

public interface FieldResolver {
    void configure(ColumnNameBuilder columnNameBuilder);
    Optional<FieldInfo> fieldInfo(Field field, Class<?> clazz);
    Optional<FieldInfo> fieldInfo(RecordComponent component, Class<?> clazz);


    record FieldInfo(
       ColumnName columnName,
       boolean optional,
       String description,
       String defaultValue
    ) {}
}
