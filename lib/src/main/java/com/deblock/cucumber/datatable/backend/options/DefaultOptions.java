package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;

public class DefaultOptions implements FullOptions {
    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        return HumanReadableColumnNameBuilder.class;
    }
}
