package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record Customer(
    @Column String firstName,
    @Column String lastName,
    @Column(value = "vip", mandatory = false, defaultValue = "false") Boolean vip
) {
}
