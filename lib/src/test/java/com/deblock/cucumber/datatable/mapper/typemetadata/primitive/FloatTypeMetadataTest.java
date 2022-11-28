package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FloatTypeMetadataTest {

    @Test
    public void shouldReturnPrimitiveFloatForDecimalDotInput() {
        final var typeMetadata = new FloatTypeMetadata();

        final var result = (float) typeMetadata.convert("10.20");

        assertThat(result).isEqualTo(10.20f);
    }

    @Test
    public void shouldReturnObjectFloatForDecimalDotInput() {
        final var typeMetadata = new FloatTypeMetadata();

        final var result = (Float) typeMetadata.convert("10.25");

        assertThat(result).isEqualTo(10.25f);
    }

    @Test
    public void shouldReturnFloatForIntValue() {
        final var typeMetadata = new FloatTypeMetadata();

        final var result = typeMetadata.convert("10");

        assertThat(result).isEqualTo(10.0f);
    }

    @Test
    public void shouldReturnFloatForDecimalCommaInput() {
        final var typeMetadata = new FloatTypeMetadata();

        final var result = typeMetadata.convert("5,20");

        assertThat(result).isEqualTo(5.20f);
    }
}
