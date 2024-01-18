package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.Objects;

@DataTableWithHeader
public class BeanWithNestedObjects {

    @Column("column")
    public String column;
    @Column
    public NestedObject nestedObjectAllMandatory;
    @Column
    private NestedObject2 nestedObjectWithOptional;

    public BeanWithNestedObjects() {

    }

    public void setNestedObjectWithOptional(NestedObject2 nestedObjectWithOptional) {
        this.nestedObjectWithOptional = nestedObjectWithOptional;
    }

    public NestedObject2 getNestedObjectWithOptional() {
        return this.nestedObjectWithOptional;
    }

    @DataTableWithHeader
    public static class NestedObject {
        @Column("column1")
        public String nestedColumn1;
        @Column("column2")
        private String nestedColumn2;

        public NestedObject() {

        }

        public NestedObject(String nestedColumn1, String nestedColumn2) {
            this.nestedColumn1 = nestedColumn1;
            this.nestedColumn2 = nestedColumn2;
        }

        public String getNestedColumn2() {
            return nestedColumn2;
        }

        public void setNestedColumn2(String nestedColumn2) {
            this.nestedColumn2 = nestedColumn2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NestedObject that = (NestedObject) o;
            return Objects.equals(nestedColumn1, that.nestedColumn1) && Objects.equals(nestedColumn2, that.nestedColumn2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nestedColumn1, nestedColumn2);
        }
    }

    @DataTableWithHeader
    public static class NestedObject2 {
        @Column("column1")
        public String nestedColumn1;
        @Column(value = "column3", mandatory = false)
        public String nestedColumn3;

        public NestedObject2() {

        }
        public NestedObject2(String nestedColumn1, String nestedColumn3) {
            this.nestedColumn1 = nestedColumn1;
            this.nestedColumn3 = nestedColumn3;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NestedObject2 that = (NestedObject2) o;
            return Objects.equals(nestedColumn1, that.nestedColumn1) && Objects.equals(nestedColumn3, that.nestedColumn3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nestedColumn1, nestedColumn3);
        }
    }
}
