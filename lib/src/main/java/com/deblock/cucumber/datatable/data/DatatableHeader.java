package com.deblock.cucumber.datatable.data;

import java.util.List;

public class DatatableHeader {
    public final List<String> names;
    public final String description;
    public final boolean optional;
    public final String defaultValue;
    public final TypeMetadata typeMetadata;

    public DatatableHeader(List<String> names, String description, boolean optional, String defaultValue, TypeMetadata typeMetadata) {
        this.names = names;
        this.description = description;
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.typeMetadata = typeMetadata;
    }

    public boolean match(String name) {
        return this.names.contains(name);
    }

    @Override
    public String toString() {
        return "DatatableHeader{" +
                "names=" + names +
                ", description='" + description + '\'' +
                ", optional=" + optional +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }

    public String getSample() {
        return this.typeMetadata.getSample();
    }

    public String getTypeDescription() {
        return this.typeMetadata.getTypeDescription();
    }
}