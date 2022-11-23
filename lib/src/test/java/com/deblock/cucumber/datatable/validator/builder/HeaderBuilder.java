package com.deblock.cucumber.datatable.validator.builder;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import jdk.jshell.spi.ExecutionControl;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Type;
import java.util.List;

public class HeaderBuilder {

    private final List<String> names;
    private boolean mandatory;
    private String description;
    private String defaultValue;
    private TypeMetadata typeMetadata;

    public HeaderBuilder(List<String> names) {
        this.names = names;
        this.typeMetadata = typeMetadata("string", "string");
    }

    public static HeaderBuilder header(String... names) {
        return new HeaderBuilder(List.of(names));
    }

    public static TypeMetadata typeMetadata(String sample, String typeDescription) {
        final var typeMetadataMock = Mockito.mock(TypeMetadata.class);
        Mockito.when(typeMetadataMock.getSample()).thenReturn(sample);
        Mockito.when(typeMetadataMock.getTypeDescription()).thenReturn(typeDescription);
        return typeMetadataMock;
    }

    public HeaderBuilder mandatory() {
        this.mandatory = true;
        return this;
    }

    public HeaderBuilder description(String description) {
        this.description = description;
        return this;
    }

    public HeaderBuilder defaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public HeaderBuilder typeMetadata(TypeMetadata typeMetadata) {
        this.typeMetadata = typeMetadata;
        return this;
    }

    public DatatableHeader build() {
        return new DatatableHeader(this.names, this.description, !this.mandatory, this.defaultValue, this.typeMetadata);
    }
}
