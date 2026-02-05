package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public record RecordBean(
    String string,
    Integer integer,
    List<Integer> integers,
    EnumValue enumValue,
    CustomDTO customDTO,
    OtherCustomDTO otherCustomDTO
) {
}
