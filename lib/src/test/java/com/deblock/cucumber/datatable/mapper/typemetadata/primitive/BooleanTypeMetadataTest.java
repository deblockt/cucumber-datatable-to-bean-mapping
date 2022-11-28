package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BooleanTypeMetadataTest {

    @Test
    public void shouldReturnPrimitiveTrueForTrueInput() {
        final var typeMetadata = new BooleanTypeMetadata();

        final var result = (boolean) typeMetadata.convert("true");

        assertThat(result).isEqualTo(true);
    }

    @Test
    public void shouldReturnObjectTrueForTrueInput() {
        final var typeMetadata = new BooleanTypeMetadata();

        final var result = (Boolean) typeMetadata.convert("true");

        assertThat(result).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void shouldReturnObjectFalseForNonTrueInput() {
        final var typeMetadata = new BooleanTypeMetadata();

        final var result = (Boolean) typeMetadata.convert("a");

        assertThat(result).isEqualTo(Boolean.FALSE);
    }

    @Test
    public void shouldReturnPrimitiveFalseForNonTrueInput() {
        final var typeMetadata = new BooleanTypeMetadata();

        final var result = (boolean) typeMetadata.convert("b");

        assertThat(result).isEqualTo(false);
    }
}
