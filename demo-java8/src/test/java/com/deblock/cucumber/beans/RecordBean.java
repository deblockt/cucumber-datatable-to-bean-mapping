package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;
import java.util.Map;

@DataTableWithHeader
public record RecordBean(
    @Column(value = "string")
    String string,
    @Column(value = "integer")
    Integer integer,
    @Column(value = "integers", mandatory = false)
    List<Integer> integers,
    @Column(value = "enumValue", defaultValue = "VALUE2")
    EnumValue enumValue,
    @Column(value = "customDTO", mandatory = false)
    CustomDTO customDTO,
    @Column(value = "aMap", mandatory = false)
    Map<String, String> aMap
) {
}
