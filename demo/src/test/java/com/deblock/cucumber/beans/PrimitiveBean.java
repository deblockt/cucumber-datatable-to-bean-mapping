package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public class PrimitiveBean {

    @Column(value = "string")
    public String string;

    @Column(value = "integer")
    public Integer integer;

    @Column(value = "integers", mandatory = false)
    public List<Integer> integers;

    @Column(value = "enumValue", defaultValue = "VALUE2")
    public EnumValue enumValue;

    @Column(value = "customDTO", mandatory = false)
    public CustomDTO customDTO;

    @Override
    public String toString() {
        return "PrimitiveBean{" +
                "string='" + string + '\'' +
                ", integer=" + integer +
                ", integers=" + integers +
                ", enumValue=" + enumValue +
                ", customDTO=" + customDTO +
                '}';
    }
}
