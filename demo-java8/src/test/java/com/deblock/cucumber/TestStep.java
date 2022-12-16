package com.deblock.cucumber;

import com.deblock.cucumber.beans.CustomDTO;
import com.deblock.cucumber.beans.PrimitiveBean;
import com.deblock.cucumber.beans.RecordBean;
import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;

public class TestStep implements En {

    @CustomDatatableFieldMapper(sample = "valueString", typeDescription = "CustomDTO")
    public static CustomDTO customDTOMapper(String value) {
        return new CustomDTO(value);
    }

    public TestStep() {
        Given("a step with only one object", (PrimitiveBean bean) -> System.out.println("read: " + bean));

        Given("a step with a list", (DataTable datatable) -> System.out.println("read: " + datatable.asList(PrimitiveBean.class)));

        Given("a step with a list of record", (DataTable datatable) -> System.out.println("read: " + datatable.asList(RecordBean.class)));
    }

}
