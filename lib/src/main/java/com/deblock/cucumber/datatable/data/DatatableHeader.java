package com.deblock.cucumber.datatable.data;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.util.List;

public record DatatableHeader(
        List<String> names,
        String description,
        boolean optional,
        String defaultValue,
        TypeMetadata typeMetadata) {

    public DatatableHeader(
            Column column,
            ColumnNameBuilder columnNameBuilder,
            TypeMetadata typeMetadata
    ) {
        this(
            columnNameBuilder.build(),
            column.description(),
            !column.mandatory() || !column.defaultValue().isEmpty(),
            column.defaultValue().isEmpty() ? null : column.defaultValue(),
            typeMetadata
        );
    }

    public boolean match(String name) {
        return this.names.contains(name);
    }

    public String sample() {
        return this.typeMetadata.sample();
    }

    public String typeDescription() {
        return this.typeMetadata.typeDescription();
    }
}