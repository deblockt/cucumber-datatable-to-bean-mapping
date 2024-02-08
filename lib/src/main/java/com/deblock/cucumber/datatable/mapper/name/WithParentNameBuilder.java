package com.deblock.cucumber.datatable.mapper.name;

import java.util.List;

public class WithParentNameBuilder implements ColumnNameBuilder {
    private List<String> names;

    public WithParentNameBuilder(ColumnNameBuilder parentColumnNameBuilder, ColumnNameBuilder columnNameBuilder) {
        final List<String> currentNames = parentColumnNameBuilder == null ? List.of("") : parentColumnNameBuilder.build();
        this.names = columnNameBuilder.build().stream()
                .flatMap(name ->
                    currentNames.stream()
                            .map(currentName -> name.replaceAll("<parent_name>", currentName))
                            .distinct()
                ).toList();
    }

    @Override
    public List<String> build() {
        return this.names;
    }
}
