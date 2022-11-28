package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IntTypeMetadataTest {

    @Test
    public void shouldReturnPrimitiveIntForIntegerInput() {
        final var typeMetadata = new IntTypeMetadata();

        final var result = (int) typeMetadata.convert("10");

        assertThat(result).isEqualTo(10);
    }

    @Test
    public void shouldReturnIntegerForIntegerInput() {
        final var typeMetadata = new IntTypeMetadata();

        final var result = (Integer) typeMetadata.convert("10");

        assertThat(result).isEqualTo(10);
    }

    @Test
    public void shouldFailForDecimalInput() {
        final var typeMetadata = new IntTypeMetadata();

        assertThrows(NumberFormatException.class, () -> typeMetadata.convert("10.45"));
    }

}
