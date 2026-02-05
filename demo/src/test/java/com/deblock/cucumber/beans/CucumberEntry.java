package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record CucumberEntry(String firstName, String lastName) {
}
