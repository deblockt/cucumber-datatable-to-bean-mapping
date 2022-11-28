package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LongTypeMetadataTest {

    @Test
    public void shouldReturnPrimitiveLongForIntegerInput() {
        final var typeMetadata = new LongTypeMetadata();

        final var result = (long) typeMetadata.convert("10");

        assertThat(result).isEqualTo(10);
    }

    @Test
    public void shouldReturnObjectLongForIntegerInput() {
        final var typeMetadata = new LongTypeMetadata();

        final var result = (Long) typeMetadata.convert("10");

        assertThat(result).isEqualTo(10);
    }

    @Test
    public void shouldFailForDecimalInput() {
        final var typeMetadata = new LongTypeMetadata();

        Assertions.assertThrows(NumberFormatException.class, () -> typeMetadata.convert("10.45"));
    }

}
