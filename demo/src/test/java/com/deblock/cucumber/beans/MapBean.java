package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;
import java.util.Map;

@DataTableWithHeader
public record MapBean(
    String name,
    Map<String, List<Integer>> scores
) {
}
