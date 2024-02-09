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
    @Column(mandatory = false)
    public BeanWithNestedObjects.NestedObject3 optionalNestedObjectAllMandatory;
    @Column("nestedWithCustomMapper")
    public BeanWithNestedObjects.NestedWithCustomMapper nestedWithCustomMapper;

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
        @Column(value = "column1", description = "the column1")
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
        @Column(value = {"column1", "column 1"}, description = "the column1 on second object")
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

    @DataTableWithHeader
    public static class NestedObject3 {
        @Column("column4")
        private String nestedColumn4;
        @Column(value = "column3", mandatory = false)
        private String nestedColumn3;

        public String getNestedColumn4() {
            return nestedColumn4;
        }

        public void setNestedColumn4(String nestedColumn4) {
            this.nestedColumn4 = nestedColumn4;
        }

        public String getNestedColumn3() {
            return nestedColumn3;
        }

        public void setNestedColumn3(String nestedColumn3) {
            this.nestedColumn3 = nestedColumn3;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            NestedObject3 that = (NestedObject3) object;
            return Objects.equals(nestedColumn4, that.nestedColumn4) && Objects.equals(nestedColumn3, that.nestedColumn3);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nestedColumn4, nestedColumn3);
        }
    }

    @DataTableWithHeader
    public static class NestedWithCustomMapper {

        @Column
        public String ignoredColumn;

        public NestedWithCustomMapper(String ignoredColumn) {
            this.ignoredColumn = ignoredColumn;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            NestedWithCustomMapper that = (NestedWithCustomMapper) object;
            return Objects.equals(ignoredColumn, that.ignoredColumn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ignoredColumn);
        }
    }
}
