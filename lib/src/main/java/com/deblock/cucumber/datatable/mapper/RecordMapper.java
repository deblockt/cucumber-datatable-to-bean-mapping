package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class RecordMapper implements DatatableMapper {
    private final Class<?> clazz;
    private final List<DatatableHeader> headers = new ArrayList<>();

    private final List<Function<Map<String, String>, Object>> fieldConverters = new ArrayList<>();
    private final Constructor<?> constructor;

    public RecordMapper(Class<?> recordClass, TypeMetadataFactory typeMetadataFactory) {
        this.clazz = recordClass;

        final var components = clazz.getRecordComponents();
        for (final RecordComponent recordComponent : components) {
            if (recordComponent.isAnnotationPresent(Column.class)) {
                final var datatableHeader = this.buildFieldData(recordComponent, typeMetadataFactory);
                this.headers.add(datatableHeader);
                this.fieldConverters.add(entry -> {
                    for (final var headerName : datatableHeader.names()) {
                        if (entry.containsKey(headerName)) {
                            return datatableHeader.typeMetadata().convert(entry.get(headerName));
                        }
                    }
                    if (datatableHeader.defaultValue() == null) {
                        return null;
                    }
                    return datatableHeader.typeMetadata().convert(datatableHeader.defaultValue());
                });
            } else {
                this.fieldConverters.add(entry -> null);
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
        return this.headers;
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final var values = this.fieldConverters.stream()
                .map(converter -> converter.apply(entry))
                .toArray();
        try {
            return constructor.newInstance(values);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private DatatableHeader buildFieldData(RecordComponent recordComponent, TypeMetadataFactory typeMetadataFactory) {
        final var column = recordComponent.getAnnotation(Column.class);

        return new DatatableHeader(
            column,
            recordComponent.getName(),
            typeMetadataFactory.build(recordComponent.getGenericType())
        );
    }
}
