package com.deblock.cucumber.datatable.data;

import java.util.List;

public record DatatableHeader(
        List<String> names,
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