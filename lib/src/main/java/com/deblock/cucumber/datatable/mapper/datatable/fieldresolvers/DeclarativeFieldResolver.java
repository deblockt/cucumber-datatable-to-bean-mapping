package com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Optional;

/**
 * Resolve as a field only fields with @Column annotation
 */
public class DeclarativeFieldResolver implements FieldResolver {
    private ColumnNameBuilder columnNameBuilder;

    @Override
    public void configure(ColumnNameBuilder columnNameBuilder) {
        this.columnNameBuilder = columnNameBuilder;
    }

    @Override
    public Optional<FieldInfo> fieldInfo(Field field, Class<?> clazz) {
        return fromAnnotatedElement(field, field.getName());
    }

    @Override
    public Optional<FieldInfo> fieldInfo(RecordComponent component, Class<?> clazz) {
        return fromAnnotatedElement(component, component.getName());
    }

    private Optional<FieldInfo> fromAnnotatedElement(AnnotatedElement element, String fieldName) {
        if (element.isAnnotationPresent(Column.class)) {
            Column annotation = element.getAnnotation(Column.class);
            return Optional.of(
                    new FieldInfo(
                            new ColumnName(
                                    annotation.value().length == 0 ? columnNameBuilder.build(fieldName) : Arrays.asList(annotation.value())
                            ),
                            !annotation.mandatory() || !annotation.defaultValue().isEmpty(),
                            annotation.description(),
                            annotation.defaultValue().isEmpty() ? null : annotation.defaultValue()
                    )
            );
        }
        return Optional.empty();
    }
}
