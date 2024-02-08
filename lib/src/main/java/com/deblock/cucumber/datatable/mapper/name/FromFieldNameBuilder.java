package com.deblock.cucumber.datatable.mapper.name;

import java.lang.reflect.Field;
import java.util.List;

public class FromFieldNameBuilder implements ColumnNameBuilder {

    private final List<String> names;

    public FromFieldNameBuilder(Field field) {
        this.names = List.of(humanName(field.getName()), field.getName());
    }

    @Override
    public List<String> build() {
        return this.names;
    }

    private static String humanName(String name) {
        return name.replaceAll("([a-z])([A-Z])", "$1 $2").trim().toLowerCase();
    }
}
