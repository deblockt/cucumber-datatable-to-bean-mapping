package com.deblock.cucumber.datatable.mapper.name;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HumanReadableColumnNameBuilderTest {

    @ParameterizedTest
    @CsvSource({
            "fieldName,field name",
            "FieldNameMultiWords,field name multi words",
            "FieldNameDTO,field name dto",
            "field2,field2"
    })
    public void shouldReturnFieldNameLowerCasesWithSpaces(String input, String expected) {
        final var builder = new HumanReadableColumnNameBuilder();

        final var result = builder.build(input);

        assertThat(result).isEqualTo(List.of(expected));
    }
}
