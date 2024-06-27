package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public abstract class BaseObjectDatatableMapper<T extends DatatableMapper> implements DatatableMapper {

    protected final List<DatatableHeader> headers;
    protected final List<T> fields;

    protected BaseObjectDatatableMapper(List<T> mappers) {
        this.headers = mergeHeaders(mappers.stream().flatMap(it -> it.headers().stream()).toList());
        this.fields = mappers;
    }

    @Override
    public List<DatatableHeader> headers() {
        return this.headers;
    }

    private static List<DatatableHeader> mergeHeaders(List<DatatableHeader> headers) {
        final var mergedHeaders = new ArrayList<DatatableHeader>();
        for (DatatableHeader header: headers) {
            final var sameHeaders = mergedHeaders.stream()
                    .filter(it -> it.names().hasOneNameEquals(header.names()))
                    .toList();
            if (sameHeaders.isEmpty()) {
                mergedHeaders.add(header);
            } else {
                final var headersToMerge = new ArrayList<>(sameHeaders);
                headersToMerge.add(header);
                final var mergedHeader = new DatatableHeader(
                        ColumnName.merge(headersToMerge.stream().map(DatatableHeader::names).toList()),
                        join(". ", headersToMerge.stream().map(DatatableHeader::description), ""),
                        headersToMerge.stream().anyMatch(DatatableHeader::optional),
                        join(" | ", headersToMerge.stream().map(DatatableHeader::defaultValue), null),
                        header.typeMetadata()
                );
                mergedHeaders.removeAll(sameHeaders);
                mergedHeaders.add(mergedHeader);
            }
        }
        return mergedHeaders;
    }

    private static String join(String delimiter, Stream<String> stringStream, String defaultValue) {
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
}
