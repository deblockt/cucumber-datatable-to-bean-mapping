package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public record RecordWithUnsupportedConverter(
        @Column
        CustomBeanWithoutMapper customBean
) { }
