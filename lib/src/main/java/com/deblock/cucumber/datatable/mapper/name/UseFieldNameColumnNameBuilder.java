package com.deblock.cucumber.datatable.mapper.name;

import java.util.List;

public class UseFieldNameColumnNameBuilder implements ColumnNameBuilder {
    @Override
    public List<String> build(String fieldName) {
        return List.of(fieldName);
    }
}
