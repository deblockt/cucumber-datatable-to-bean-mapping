package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class PrimitiveBean {

    @Column(value = "string")
    public String string;

    @Column(value = "integer")
    public Integer integer;

    @Override
    public String toString() {
        return "PrimitiveBean{" +
                "string='" + string + '\'' +
                ", integer=" + integer +
                '}';
    }
}
