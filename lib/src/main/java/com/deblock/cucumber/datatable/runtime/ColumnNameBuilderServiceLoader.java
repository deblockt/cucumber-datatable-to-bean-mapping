package com.deblock.cucumber.datatable.runtime;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.util.function.Supplier;

public class ColumnNameBuilderServiceLoader extends AbstractServiceLoader<ColumnNameBuilder> {
    public ColumnNameBuilderServiceLoader(Options options, Supplier<ClassLoader> classLoaderSupplier) {
        super(ColumnNameBuilder.class, options.getNameBuilderClass(), classLoaderSupplier);
    }
}
