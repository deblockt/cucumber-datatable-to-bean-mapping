package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.data.DatatableHeader;

import java.util.List;
import java.util.Map;

public interface DatatableMapper {

    List<DatatableHeader> headers();

    Object convert(Map<String, String> entry);
}
