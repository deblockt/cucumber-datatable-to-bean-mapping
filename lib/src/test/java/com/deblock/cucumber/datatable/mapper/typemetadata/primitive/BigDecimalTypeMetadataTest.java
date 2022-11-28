package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalTypeMetadataTest {

    @Test
    public void shouldReturnABigDecimalFromInputWithDotSeparator() {
        final var typeMetadata = new BigDecimalTypeMetadata();

        final var result = typeMetadata.convert("10.15");

        assertThat(result).isEqualTo(new BigDecimal("10.15"));
    }

    @Test
    public void shouldReturnABigDecimalFromInputWithCommaSeparator() {
        final var typeMetadata = new BigDecimalTypeMetadata();

        final var result = typeMetadata.convert("10,15");

        assertThat(result).isEqualTo(new BigDecimal("10.15"));
    }

    @Test
    public void shouldReturnABigDecimalFromInputWithoutDecimalPart() {
        final var typeMetadata = new BigDecimalTypeMetadata();

        final var result = typeMetadata.convert("10");

        assertThat(result).isEqualTo(new BigDecimal("10"));
    }
}
