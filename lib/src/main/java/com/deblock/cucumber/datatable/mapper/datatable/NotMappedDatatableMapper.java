package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;

import java.util.List;
import java.util.Map;

/**
 * This class is used to represent a class field who is not mapped on datatable
 */
public class NotMappedDatatableMapper implements DatatableMapper {
    @Override
    public List<DatatableHeader> headers() {
        return List.of();
    }

    @Override
    public Object convert(Map<String, String> entry) {
        return null;
    }
}
