package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class MalformedBeanPrivateColumnWithoutSetter {
    @Column(value = "intProp", description = "a property with primitive int", defaultValue = "10")
    private int intProp;
}