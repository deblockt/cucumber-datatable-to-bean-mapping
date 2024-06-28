package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;

public class DefaultOptions implements FullOptions {
    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        return HumanReadableColumnNameBuilder.class;
    }

    @Override
    public Class<? extends FieldResolver> getFieldResolverClass() {
        return DeclarativeFieldResolver.class;
    }
}
