package com.deblock.cucumber.datatable.data;

import java.util.List;
import java.util.Map;

public class NotMappedColumnField implements ColumnField {
    @Override
    public List<DatatableHeader> headers() {
        return List.of();
    }

    @Override
    public Object convert(Map<String, String> entry) {
        return null;
    }
}
