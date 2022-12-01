package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public record Record(
    @Column("stringProp")
    String prop,
    @Column(value = "intProp", description = "a property with primitive int", defaultValue = "10")
    int intProp,
    @Column(value = {"other bean", "my bean"}, mandatory = false)
    OtherBean otherBean,
    String nonAnnotatedColumn,
    @Column(value = "list")
    List<String> list
) {
    public static class OtherBean {

    }
}