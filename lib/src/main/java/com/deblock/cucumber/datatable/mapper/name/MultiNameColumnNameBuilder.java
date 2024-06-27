package com.deblock.cucumber.datatable.mapper.name;

import java.util.List;
import java.util.stream.Stream;

public class MultiNameColumnNameBuilder implements ColumnNameBuilder {
    private final HumanReadableColumnNameBuilder humanReadableColumnNameBuilder;
    private final UseFieldNameColumnNameBuilder useFieldNameColumnNameBuilder;

    public MultiNameColumnNameBuilder() {
        this.humanReadableColumnNameBuilder = new HumanReadableColumnNameBuilder();
        this.useFieldNameColumnNameBuilder = new UseFieldNameColumnNameBuilder();
    }

    @Override
    public List<String> build(String fieldName) {
        return Stream.concat(
                this.humanReadableColumnNameBuilder.build(fieldName).stream(),
                this.useFieldNameColumnNameBuilder.build(fieldName).stream()
        ).toList();
    }
}
