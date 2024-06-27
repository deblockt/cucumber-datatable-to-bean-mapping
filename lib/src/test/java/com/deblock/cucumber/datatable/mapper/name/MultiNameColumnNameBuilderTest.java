package com.deblock.cucumber.datatable.mapper.name;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiNameColumnNameBuilderTest {

    @ParameterizedTest
    @CsvSource({
            "fieldName,field name",
            "FieldNameMultiWords,field name multi words",
            "FieldNameDTO,field name dto"
    })
    public void shouldReturnHumanReadableAndFieldName(String fieldName, String humanReadable) {
        final var builder = new MultiNameColumnNameBuilder();

        final var result = builder.build(fieldName);

        assertThat(result).isEqualTo(List.of(humanReadable, fieldName));
    }
}
