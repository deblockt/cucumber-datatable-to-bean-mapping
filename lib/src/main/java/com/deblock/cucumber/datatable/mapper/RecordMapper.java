package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.ColumnField;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.NotMappedColumnField;
import com.deblock.cucumber.datatable.data.SimpleColumnField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordMapper implements DatatableMapper {
    private final Class<?> clazz;
    private final List<ColumnField> fields = new ArrayList<>();
    private final Constructor<?> constructor;

    public RecordMapper(Class<?> recordClass, TypeMetadataFactory typeMetadataFactory) {
        this.clazz = recordClass;

        final var components = clazz.getRecordComponents();
        for (final RecordComponent recordComponent : components) {
            if (recordComponent.isAnnotationPresent(Column.class)) {
                final var datatableHeader = this.buildFieldData(recordComponent, typeMetadataFactory);
                this.fields.add(datatableHeader);
            } else {
                this.fields.add(new NotMappedColumnField());
            }
        }

        final var types = Arrays.stream(components)
                .map(RecordComponent::getType)
                .toArray(Class<?>[]::new);
        try {
            this.constructor = this.clazz.getDeclaredConstructor(types);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DatatableHeader> headers() {
        return this.fields.stream()
                .flatMap(it -> it.headers().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final var values = this.fields.stream()
                .map(it -> it.convert(entry))
                .toArray();
        try {
            return constructor.newInstance(values);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private ColumnField buildFieldData(RecordComponent recordComponent, TypeMetadataFactory typeMetadataFactory) {
        final var column = recordComponent.getAnnotation(Column.class);

        return new SimpleColumnField(column, recordComponent.getName(), recordComponent.getGenericType(), typeMetadataFactory);
    }
}
