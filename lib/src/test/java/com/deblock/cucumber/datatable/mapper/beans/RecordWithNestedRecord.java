package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record RecordWithNestedRecord(
        @Column("column")
        String column,
        @Column
        NestedObject nestedObjectAllMandatory,
        @Column
        NestedObject2 nestedObjectWithOptional,
        @Column(mandatory = false)
        NestedObject3 optionalNestedObjectAllMandatory,
        @Column("nestedWithCustomMapper")
        NestedWithCustomMapper nestedWithCustomMapper
) {

    @DataTableWithHeader
    public record NestedObject(
            @Column(value = "column1", description = "the column1")
            String nestedColumn1,
            @Column("column2")
            String nestedColumn2
    ) {}

    @DataTableWithHeader
    public record NestedObject2(
            @Column(value = {"column1", "column 1"}, description = "the column1 on second object")
            String nestedColumn1,
            @Column(value = "column3", mandatory = false)
            String nestedColumn3
    ) {}

    @DataTableWithHeader
    public record NestedObject3(
            @Column("column4")
            String nestedColumn4,
            @Column(value = "column3", mandatory = false)
            String nestedColumn3
    ) {}

    @DataTableWithHeader
    public record NestedWithCustomMapper(
            @Column
            String ignoredColumn
    ) {}
}
