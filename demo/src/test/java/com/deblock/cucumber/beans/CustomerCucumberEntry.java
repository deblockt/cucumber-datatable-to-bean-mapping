package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record CustomerCucumberEntry(
        String firstName,
        String lastName
) {
}
