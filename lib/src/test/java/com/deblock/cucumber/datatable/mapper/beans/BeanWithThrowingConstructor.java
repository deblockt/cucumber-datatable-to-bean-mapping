package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class BeanWithThrowingConstructor {
    @Column("value")
    public String value;

    public BeanWithThrowingConstructor() {
        throw new IllegalStateException("constructor failure");
    }
}
