package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public record RecordBean(
    @Column(value = "string")
    String string,
    @Column(value = "integer")
    Integer integer,
    @Column(value = "integers", mandatory = false)
    List<Integer> integers
) {
}
