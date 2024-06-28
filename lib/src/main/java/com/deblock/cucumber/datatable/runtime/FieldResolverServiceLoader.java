package com.deblock.cucumber.datatable.runtime;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import io.cucumber.core.exception.CucumberException;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public class FieldResolverServiceLoader {
    private final Options options;
    private final Supplier<ClassLoader> classLoaderSupplier;
    private final ColumnNameBuilderServiceLoader columnNameBuilderServiceLoader;

    public FieldResolverServiceLoader(Options options, Supplier<ClassLoader> classLoaderSupplier, ColumnNameBuilderServiceLoader columnNameBuilderServiceLoader) {
        this.options = options;
        this.classLoaderSupplier = classLoaderSupplier;
        this.columnNameBuilderServiceLoader = columnNameBuilderServiceLoader;
    }

    public FieldResolver loadFieldResolverBuilder() {
        ServiceLoader<FieldResolver> loader = ServiceLoader.load(FieldResolver.class, classLoaderSupplier.get());
        Class<? extends FieldResolver> expectedClass = this.options.getFieldResolverClass();

        for (FieldResolver fieldResolver : loader) {
            if (expectedClass.equals(fieldResolver.getClass())) {
                fieldResolver.configure(this.columnNameBuilderServiceLoader.loadColumnNameBuilder());
                return fieldResolver;
            }
        }

        throw new CucumberException(
                """
                Could not find field resolver %s.
                Cucumber uses SPI to discover name builder implementations.
                Has the class been registered with SPI and is it available on the classpath?
                """.formatted(expectedClass.getName())
        );
    }
}
