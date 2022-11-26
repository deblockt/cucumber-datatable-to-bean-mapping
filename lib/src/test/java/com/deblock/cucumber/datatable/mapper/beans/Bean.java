package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public class Bean {
    @Column("stringProp")
    public String prop;

    @Column(value = "intProp", description = "a property with primitive int", defaultValue = "10")
    public int intProp;

    @Column(value = {"other bean", "my bean"}, mandatory = false)
    public OtherBean otherBean;

    @Column(value = "private list")
    private List<String> privateList;

    private String nonAnnotatedColumn;

    public static class OtherBean {

    }

    public List<String> getPrivateList() {
        return privateList;
    }

    public void setPrivateList(List<String> privateList) {
        this.privateList = privateList;
    }
}