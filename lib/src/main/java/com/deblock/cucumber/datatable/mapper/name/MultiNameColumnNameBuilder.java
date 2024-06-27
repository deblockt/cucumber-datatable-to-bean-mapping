package com.deblock.cucumber.datatable.mapper.name;

import java.util.List;
import java.util.stream.Stream;

public class MultiNameColumnNameBuilder implements DataTableColumnNameBuilder {
    private final HumanReadableColumnNameBuilder humanReadableColumnNameBuilder;
    private final StrictFieldNameColumnNameBuilder strictFieldNameColumnNameBuilder;

    public MultiNameColumnNameBuilder() {
        this.humanReadableColumnNameBuilder = new HumanReadableColumnNameBuilder();
        this.strictFieldNameColumnNameBuilder = new StrictFieldNameColumnNameBuilder();
    }

    @Override
    public List<String> build(String fieldName) {
        return Stream.concat(
                this.humanReadableColumnNameBuilder.build(fieldName).stream(),
                this.strictFieldNameColumnNameBuilder.build(fieldName).stream()
        ).toList();
    }
}
