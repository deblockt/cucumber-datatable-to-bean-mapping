package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record PrimitiveValues(
    @Column("int") int anInt,
    @Column("long") long aLong,
    @Column("short") short aShort,
    @Column("float") float aFloat,
    @Column("double") double aDouble,
    @Column("boolean") boolean aBoolean
) {
}
