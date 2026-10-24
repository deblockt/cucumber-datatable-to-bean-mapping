package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class BeanWithThrowingSetter {
    @Column("value")
    private String value;

    public void setValue(String value) {
        throw new IllegalStateException("invalid value " + value);
    }
}
