package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MapperFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class RecordDatatableMapper implements DatatableMapper {
    private final Class<?> clazz;
    private final List<DatatableMapper> fields = new ArrayList<>();
    private final Constructor<?> constructor;

    public RecordDatatableMapper(Class<?> recordClass, MapperFactory mapperFactory) {
        this.clazz = recordClass;

        final var components = clazz.getRecordComponents();
        for (final RecordComponent recordComponent : components) {
            if (recordComponent.isAnnotationPresent(Column.class)) {
                final var datatableHeader = this.buildFieldData(recordComponent, mapperFactory);
                this.fields.add(datatableHeader);
            } else {
                this.fields.add(new NotMappedDatatableMapper());
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
        final var headers = this.fields.stream()
                .flatMap(it -> it.headers().stream())
                .toList();
        final var mergedHeaders = new ArrayList<DatatableHeader>();
        for (DatatableHeader header: headers) {
            final var sameHeaders = mergedHeaders.stream()
                    .filter(it -> !Collections.disjoint(it.names(), header.names()))
                    .toList();
            if (sameHeaders.isEmpty()) {
                mergedHeaders.add(header);
            } else {
                final var headersToMerge = new ArrayList<>(sameHeaders);
                headersToMerge.add(header);
                final var mergedHeader = new DatatableHeader(
                    headersToMerge.stream().flatMap(it -> it.names().stream()).distinct().toList(),
                    join(". ", headersToMerge.stream().map(DatatableHeader::description), ""),
                    headersToMerge.stream().anyMatch(DatatableHeader::optional),
                    join(" | ", headersToMerge.stream().map(DatatableHeader::defaultValue), null),
                    header.typeMetadata()
                );
                mergedHeaders.add(mergedHeader);
                mergedHeaders.removeAll(sameHeaders);
            }
        }
        return mergedHeaders;
    }

    private String join(String delimiter, Stream<String> stringStream, String defaultValue) {
        return stringStream
                .filter(Objects::nonNull)
                .filter(not(String::isEmpty))
                .collect(
                        Collectors.collectingAndThen(
                            Collectors.joining(delimiter),
                            str -> str.isEmpty() ? defaultValue : str
                        )
                );
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

    private DatatableMapper buildFieldData(RecordComponent recordComponent, MapperFactory mapperFactory) {
        final var column = recordComponent.getAnnotation(Column.class);

        return mapperFactory.build(
                column,
                recordComponent.getName(),
                recordComponent.getGenericType()
        );
    }
}
