package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService;

public class DefaultOptions implements FullOptions {
    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        return HumanReadableColumnNameBuilder.class;
    }

    @Override
    public Class<? extends FieldResolver> getFieldResolverClass() {
        return DeclarativeFieldResolver.class;
    }

    @Override
    public Class<? extends DateTimeService> getDateTimeServiceClass() {
        return StaticDateTimeService.class;
    }
}
