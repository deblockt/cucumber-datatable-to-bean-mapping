package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ShortTypeMetadataTest {

    @Test
    public void shouldReturnPrimitiveShortForIntegerInput() {
        final var typeMetadata = new ShortTypeMetadata();

        final var result = (short) typeMetadata.convert("10");

        assertThat(result).isEqualTo((short) 10);
    }

    @Test
    public void shouldReturnObjectShortForIntegerInput() {
        final var typeMetadata = new ShortTypeMetadata();

        final var result = (Short) typeMetadata.convert("10");

        assertThat(result).isEqualTo(Short.valueOf("10"));
    }

    @Test
    public void shouldFailForDecimalInput() {
        final var typeMetadata = new ShortTypeMetadata();

        Assertions.assertThrows(NumberFormatException.class, () -> typeMetadata.convert("10.45"));
    }

}
