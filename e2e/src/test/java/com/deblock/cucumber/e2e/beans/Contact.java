package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record Contact(
    @Column("<parent_name> id") String id,
    @Column("<parent_name>") PersonalInformation personalInformation
) {
}
