package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class Product {
    @Column
    public String name;

    @Column(mandatory = false)
    public Integer stock;

    @Override
    public String toString() {
        return "Product[name=" + name + ", stock=" + stock + "]";
    }
}
