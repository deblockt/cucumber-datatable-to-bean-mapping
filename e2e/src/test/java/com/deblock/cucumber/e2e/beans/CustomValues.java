package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public record CustomValues(
    @Column(value = "code", mandatory = false) Code code,
    @Column(value = "codes", mandatory = false) List<Code> codes,
    @Column(value = "reference", mandatory = false) Reference reference
) {
}
