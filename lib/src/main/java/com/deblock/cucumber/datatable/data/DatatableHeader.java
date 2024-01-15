package com.deblock.cucumber.datatable.data;

import com.deblock.cucumber.datatable.annotations.Column;

import java.util.List;

public record DatatableHeader(
        List<String> names,
        String description,
        boolean optional,
        String defaultValue,
        TypeMetadata typeMetadata) {

    public DatatableHeader(
            Column column,
            String originalFieldName,
            TypeMetadata typeMetadata
    ) {
        this(
            buildNames(column.value(), originalFieldName),
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

    private static List<String> buildNames(String[] names, String fieldName) {
        if (names != null && names.length > 0) {
            return List.of(names);
        }
        return List.of(humanName(fieldName), fieldName);
    }

    private static String humanName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1 $2").trim().toLowerCase();
    }
}