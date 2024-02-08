package com.deblock.cucumber.datatable.mapper.name;

import com.deblock.cucumber.datatable.annotations.Column;

import java.util.Arrays;
import java.util.List;

public class FromAnnotationColumnNameBuilder implements ColumnNameBuilder {

    private final List<String> names;

    public FromAnnotationColumnNameBuilder(Column column) {
        this.names = Arrays.asList(column.value());
    }

    @Override
    public List<String> build() {
        return this.names;
    }
}
