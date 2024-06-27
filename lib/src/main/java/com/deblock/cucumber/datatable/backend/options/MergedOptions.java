package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.Options;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MergedOptions implements FullOptions {
    private final List<FullOptions> options;

    public MergedOptions(FullOptions ...options) {
        this.options = Arrays.asList(options);
    }

    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        return this.options.stream()
                .map(Options::getNameBuilderClass)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
