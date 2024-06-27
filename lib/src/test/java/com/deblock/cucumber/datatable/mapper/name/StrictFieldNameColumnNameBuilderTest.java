package com.deblock.cucumber.datatable.mapper.name;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StrictFieldNameColumnNameBuilderTest {

    @ParameterizedTest
    @CsvSource({
            "fieldName",
            "FieldNameMultiWords",
            "fieldNameDTO"
    })
    public void shouldReturnFieldName(String input) {
        final var builder = new StrictFieldNameColumnNameBuilder();

        final var result = builder.build(input);

        assertThat(result).isEqualTo(List.of(input));
    }
}
