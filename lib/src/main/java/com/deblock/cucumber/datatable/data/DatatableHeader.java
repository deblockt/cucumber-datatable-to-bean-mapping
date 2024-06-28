package com.deblock.cucumber.datatable.data;

import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;

public record DatatableHeader(
        ColumnName names,
        String description,
        boolean optional,
        String defaultValue,
        TypeMetadata typeMetadata) {

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