package com.deblock.cucumber.datatable.mapper.name;

import java.util.List;

public class HumanReadableColumnNameBuilder implements DataTableColumnNameBuilder {
    @Override
    public List<String> build(String fieldName) {
        return List.of(
                fieldName.replaceAll("([a-z])([A-Z])", "$1 $2").trim().toLowerCase()
        );
    }
}
