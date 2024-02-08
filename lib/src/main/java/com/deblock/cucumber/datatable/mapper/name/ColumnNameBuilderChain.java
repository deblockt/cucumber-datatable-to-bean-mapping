package com.deblock.cucumber.datatable.mapper.name;

import java.util.Arrays;
import java.util.List;

public class ColumnNameBuilderChain implements ColumnNameBuilder {
    private List<String> names;

    public ColumnNameBuilderChain(ColumnNameBuilder ...builders) {
        this.names = Arrays.asList(builders).stream()
                .map(ColumnNameBuilder::build)
                .filter(it -> it != null && !it.isEmpty())
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<String> build() {
        return this.names;
    }
}
