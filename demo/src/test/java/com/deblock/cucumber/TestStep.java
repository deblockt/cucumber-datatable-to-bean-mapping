package com.deblock.cucumber;

import com.deblock.cucumber.beans.PrimitiveBean;
import com.deblock.cucumber.beans.RecordBean;
import io.cucumber.java.en.Given;

import java.util.List;

public class TestStep {

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
