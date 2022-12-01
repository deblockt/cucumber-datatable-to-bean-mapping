package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public record Record(
    String nonAnnotatedColumn,
    @Column("stringProp")
    String prop,
    @Column(value = "intProp", mandatory = false, description = "a property with primitive int", defaultValue = "10")
    int intProp,
    @Column(value = {"other bean", "my bean"}, mandatory = false)
    OtherBean otherBean,
    @Column(value = "list")
    List<String> list,
    @Column(value = {"mandatory with default value"}, defaultValue = "default")
    String mandatoryWithDefaultValue
) {
    public static class OtherBean {

    }
}