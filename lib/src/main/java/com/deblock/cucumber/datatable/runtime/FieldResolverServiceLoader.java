package com.deblock.cucumber.datatable.runtime;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;

import java.util.function.Supplier;

public class FieldResolverServiceLoader extends AbstractServiceLoader<FieldResolver> {
    private final ColumnNameBuilderServiceLoader columnNameBuilderServiceLoader;

    public FieldResolverServiceLoader(Options options, Supplier<ClassLoader> classLoaderSupplier, ColumnNameBuilderServiceLoader columnNameBuilderServiceLoader) {
        super(FieldResolver.class, options.getFieldResolverClass(), classLoaderSupplier);
        this.columnNameBuilderServiceLoader = columnNameBuilderServiceLoader;
    }

    @Override
    public FieldResolver loadService() {
        FieldResolver fieldResolver = super.loadService();
        fieldResolver.configure(this.columnNameBuilderServiceLoader.loadService());
        return fieldResolver;
    }
}
