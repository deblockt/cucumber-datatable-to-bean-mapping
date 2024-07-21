package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MergedOptions implements FullOptions {
    private final List<FullOptions> options;

    public MergedOptions(FullOptions ...options) {
        this.options = Arrays.asList(options);
    }

    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        return getOption(Options::getNameBuilderClass);
    }

    @Override
    public Class<? extends FieldResolver> getFieldResolverClass() {
        return getOption(Options::getFieldResolverClass);
    }

    @Override
    public Class<? extends DateTimeService> getDateTimeServiceClass() {
        return getOption(Options::getDateTimeServiceClass);
    }

    private <T> T getOption(Function<FullOptions, T> supplier) {
        return this.options.stream()
                .map(supplier)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
