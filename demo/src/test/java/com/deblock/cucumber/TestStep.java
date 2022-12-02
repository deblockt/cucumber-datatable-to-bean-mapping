package com.deblock.cucumber;

import com.deblock.cucumber.beans.CustomDTO;
import com.deblock.cucumber.beans.PrimitiveBean;
import com.deblock.cucumber.beans.RecordBean;
import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import io.cucumber.java.en.Given;

import java.util.List;

public class TestStep {

    @CustomDatatableFieldMapper(sample = "valueString", typeDescription = "CustomDTO")
    public static CustomDTO customDTOMapper(String value) {
        return new CustomDTO(value);
    }

    @Given("a step with only one object")
    public void step(PrimitiveBean bean) {
        System.out.println("read: " + bean);
    }

    @Given("a step with a list")
    public void stepWithList(List<PrimitiveBean> beans) {
        System.out.println("read: " + beans);
    }


    @Given("a step with a list of record")
    public void stepWithListOfRecord(List<RecordBean> beans) {
        System.out.println("read: " + beans);
    }
}
