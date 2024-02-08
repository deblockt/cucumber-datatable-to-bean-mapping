package com.deblock.cucumber.datatable.mapper.name;

import java.lang.reflect.RecordComponent;
import java.util.List;

public class FromComponentNameBuilder implements ColumnNameBuilder {

    private final List<String> names;

    public FromComponentNameBuilder(RecordComponent recordComponent) {
        this.names = List.of(humanName(recordComponent.getName()), recordComponent.getName());
    }
    @Override
    public List<String> build() {
        return this.names;
    }

    private static String humanName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1 $2").trim().toLowerCase();
    }
}
