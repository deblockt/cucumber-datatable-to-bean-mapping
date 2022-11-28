package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public class PrimitiveBean {

    @Column(value = "string")
    public String string;

    @Column(value = "integer")
    public Integer integer;

    @Column(value = "integers", mandatory = false)
    public List<Integer> integers;

    @Override
    public String toString() {
        return "PrimitiveBean{" +
                "string='" + string + '\'' +
                ", integer=" + integer +
                ", integers=" + integers +
                '}';
    }
}
