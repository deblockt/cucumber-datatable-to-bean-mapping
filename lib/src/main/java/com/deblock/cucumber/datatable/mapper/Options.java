package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService;

public interface Options {
    Class<? extends ColumnNameBuilder> getNameBuilderClass();
    Class<? extends FieldResolver> getFieldResolverClass();
    Class<? extends DateTimeService> getDateTimeServiceClass();
}
