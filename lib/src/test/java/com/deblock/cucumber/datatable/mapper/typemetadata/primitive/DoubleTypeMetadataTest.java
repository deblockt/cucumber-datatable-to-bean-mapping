package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DoubleTypeMetadataTest {

    @Test
    public void shouldReturnPrimitiveDoubleForDecimalDotInput() {
        final var typeMetadata = new DoubleTypeMetadata();

        final var result = (double) typeMetadata.convert("10.20");

        assertThat(result).isEqualTo(10.20);
    }

    @Test
    public void shouldReturnObjectDoubleForDecimalDotInput() {
        final var typeMetadata = new DoubleTypeMetadata();

        final var result = (Double) typeMetadata.convert("10.25");

        assertThat(result).isEqualTo(10.25);
    }

    @Test
    public void shouldReturnDoubleForIntValue() {
        final var typeMetadata = new DoubleTypeMetadata();

        final var result = typeMetadata.convert("10");

        assertThat(result).isEqualTo(10.0);
    }

    @Test
    public void shouldReturnDoubleForDecimalCommaInput() {
        final var typeMetadata = new DoubleTypeMetadata();

        final var result = typeMetadata.convert("5,20");

        assertThat(result).isEqualTo(5.20);
    }
}
