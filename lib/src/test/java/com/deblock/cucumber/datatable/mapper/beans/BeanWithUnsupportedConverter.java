package com.deblock.cucumber.datatable.mapper.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

@DataTableWithHeader
public class BeanWithUnsupportedConverter {
    @Column
    public CustomBeanWithoutMapper customBean;
}
