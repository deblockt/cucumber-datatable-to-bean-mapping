package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RecordMapper implements DatatableMapper {
    private final Class<?> clazz;
    private final List<DatatableHeader> headers = new ArrayList<>();

    private final Map<String, ComponentMetadata> componentMetadataByColumnName = new HashMap<>();

    public RecordMapper(Class<?> recordClass, TypeMetadataFactory typeMetadataFactory) {
        this.clazz = recordClass;

        final var components = clazz.getRecordComponents();
        for (int i = 0; i < components.length; i++) {
            final var recordComponent = components[i];
            if (recordComponent.isAnnotationPresent(Column.class)) {
                final var datatableHeader = this.buildFieldData(recordComponent, typeMetadataFactory);
                this.headers.add(datatableHeader);
                final var componentMetadata = new ComponentMetadata(i, datatableHeader);
                for (final var headerName: datatableHeader.names()) {
                    this.componentMetadataByColumnName.put(headerName, componentMetadata);
                }
            }
        }
    }

    @Override
    public List<DatatableHeader> headers() {
        return this.headers;
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final var types = Arrays.stream(clazz.getRecordComponents())
                .map(RecordComponent::getType)
                .toArray(Class<?>[]::new);
        final var values = new Object[types.length];

        final var nonSetComponentMetadata = new HashSet<>(this.componentMetadataByColumnName.values());
        entry.forEach((name, value) -> {
            final var componentMetadata = componentMetadataByColumnName.get(name);
            nonSetComponentMetadata.remove(componentMetadata);
            final var convertedValue = componentMetadata.header.typeMetadata().convert(value);
            values[componentMetadata.position] = convertedValue;
        });
        nonSetComponentMetadata.stream()
                .filter(componentMetadata -> componentMetadata.header.defaultValue() != null)
                .forEach(componentMetadata -> {
                    final var value = componentMetadata.header.typeMetadata().convert(componentMetadata.header.defaultValue());
                    values[componentMetadata.position] = value;
                });

        try {
            final var constructor = this.clazz.getDeclaredConstructor(types);
            return constructor.newInstance(values);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private DatatableHeader buildFieldData(RecordComponent recordComponent, TypeMetadataFactory typeMetadataFactory) {
        final var column = recordComponent.getAnnotation(Column.class);

        return new DatatableHeader(
                List.of(column.value()),
                column.description(),
                !column.mandatory() || !column.defaultValue().isEmpty(),
                column.defaultValue().isEmpty() ? null : column.defaultValue(),
                typeMetadataFactory.build(recordComponent.getGenericType())
        );
    }

    private record ComponentMetadata(
            int position,
            DatatableHeader header
    ) {

    }
}
