package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record RecordWithThrowingConstructor(
    @Column("value")
    String value
) {
    public RecordWithThrowingConstructor {
        throw new IllegalStateException("invalid value " + value);
    }
}
