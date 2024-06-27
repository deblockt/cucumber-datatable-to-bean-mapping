package com.deblock.cucumber.datatable.runtime;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import io.cucumber.core.exception.CucumberException;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public class ColumnNameBuilderServiceLoader {
    private final Options options;
    private final Supplier<ClassLoader> classLoaderSupplier;

    public ColumnNameBuilderServiceLoader(Options options, Supplier<ClassLoader> classLoaderSupplier) {
        this.options = options;
        this.classLoaderSupplier = classLoaderSupplier;
    }

    public ColumnNameBuilder loadColumnNameBuilder() {
        ServiceLoader<ColumnNameBuilder> loader = ServiceLoader.load(ColumnNameBuilder.class, classLoaderSupplier.get());
        Class<? extends ColumnNameBuilder> expectedClass = this.options.getNameBuilderClass();

        for (ColumnNameBuilder columnNameBuilder : loader) {
            if (expectedClass.equals(columnNameBuilder.getClass())) {
                return columnNameBuilder;
            }
        }

        throw new CucumberException(
                """
                Could not find name builder %s.
                Cucumber uses SPI to discover name builder implementations.
                Has the class been registered with SPI and is it available on the classpath?
                """.formatted(expectedClass.getName())
                );
    }
}
