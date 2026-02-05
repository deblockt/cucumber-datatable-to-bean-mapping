package com.deblock.cucumber.beans;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.List;

@DataTableWithHeader
public class PrimitiveBean {

    public String string;

    public Integer integer;

    public List<Integer> integers;

    public EnumValue enumValue;

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
