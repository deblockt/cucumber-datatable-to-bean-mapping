package com.deblock.cucumber.datatable.runtime;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService;

import java.util.function.Supplier;

public class DateTimeServiceLoader extends AbstractServiceLoader<DateTimeService> {
    public DateTimeServiceLoader(Options options, Supplier<ClassLoader> classLoaderSupplier) {
        super(DateTimeService.class, options.getDateTimeServiceClass(), classLoaderSupplier);
    }
}