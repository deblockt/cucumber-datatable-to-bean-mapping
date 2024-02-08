package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class BeanWithNestedRecordNameMapping {
    @Column
    public NestedObject nestedObject1;
    @Column("object2")
    private NestedObject nestedObject2;

    public void setNestedObject2(NestedObject nestedObject2) {
        this.nestedObject2 = nestedObject2;
    }

    @DataTableWithHeader
    public static class NestedObject {
        @Column(value = "<parent_name> column1", description = "the column1")
        public String nestedColumn1;
        @Column(value = "<parent_name>_2")
        private NestedObject2 nestedObject;

        public void setNestedObject(NestedObject2 nestedObject) {
            this.nestedObject = nestedObject;
        }
    }

    @DataTableWithHeader
    public static class NestedObject2 {
        @Column(value = "<parent_name> column")
        public String nestedColumn;
    }
}
