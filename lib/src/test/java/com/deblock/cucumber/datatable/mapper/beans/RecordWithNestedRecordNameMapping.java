package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record RecordWithNestedRecordNameMapping(
        @Column
        NestedObject nestedObject1,
        @Column("object2")
        NestedObject nestedObject2
) {

    @DataTableWithHeader
    public record NestedObject(
            @Column(value = "<parent_name> column1", description = "the column1")
            String nestedColumn1,
            @Column(value = "<parent_name>_2")
            NestedObject2 nestedObject
    ) {}

    @DataTableWithHeader
    public record NestedObject2(
        @Column(value = "<parent_name> column")
        String nestedColumn
    ) {}
}
