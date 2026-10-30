package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record Conversation(
    @Column(value = "customer1", mandatory = false) Contact customer1,
    @Column(value = "customer2", mandatory = false) Contact customer2
) {
}
