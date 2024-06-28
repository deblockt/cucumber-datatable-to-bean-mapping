package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Optional;

/**
 * Class use to get information on class fields.
 * This class is used to know if a field should be considered as a datatable column.
 */
public interface FieldResolver {
    void configure(ColumnNameBuilder columnNameBuilder);

    /**
     * Return field info from a bean field.
     *
     * @param field a field from class annotated with @DataTableWithHeaders
     * @param clazz the class annotated with @DataTableWithHeaders
     *
     * @return
     *  Empty if field should not be mapped on dataTable column
     *  a FieldInfo if the field should be mapped on dataTable column
     */
    Optional<FieldInfo> fieldInfo(Field field, Class<?> clazz);

    /**
     * Return field info from a record component.
     *
     * @param component a component from record annotated with @DataTableWithHeaders
     * @param clazz the record annotated with @DataTableWithHeaders
     *
     * @return
     *  Empty if field should not be mapped on dataTable column
     *  a FieldInfo if the field should be mapped on dataTable column
     */
    Optional<FieldInfo> fieldInfo(RecordComponent component, Class<?> clazz);


    record FieldInfo(
       ColumnName columnName,
       boolean optional,
       String description,
       String defaultValue
    ) {}
}
