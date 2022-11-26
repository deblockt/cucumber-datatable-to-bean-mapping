package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class MalformedBeanNoEmptyConstructor {
    @Column(value = "intProp", description = "a property with primitive int", defaultValue = "10")
    public int intProp;

    public MalformedBeanNoEmptyConstructor(String intProp) {

    }
}