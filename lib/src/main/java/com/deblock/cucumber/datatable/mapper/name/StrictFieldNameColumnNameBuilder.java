package com.deblock.cucumber.datatable.mapper.name;

import java.util.List;

public class StrictFieldNameColumnNameBuilder implements DataTableColumnNameBuilder {
    @Override
    public List<String> build(String fieldName) {
        return List.of(fieldName);
    }
}
