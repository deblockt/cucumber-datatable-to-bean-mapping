package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record PersonalInformation(
    @Column("<parent_name> first name") String firstName,
    @Column("<parent_name> last name") String lastName
) {
}
