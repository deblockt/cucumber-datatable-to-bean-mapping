package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BigIntegerTypeMetadataTest {

    @Test
    public void shouldReturnABigIntegerFromInput() {
        final var typeMetadata = new BigIntegerTypeMetadata();

        final var result = typeMetadata.convert("10");

        assertThat(result).isEqualTo(new BigInteger("10"));
    }

    @Test
    public void shouldFailForADecimalValue() {
        final var typeMetadata = new BigIntegerTypeMetadata();

        assertThrows(NumberFormatException.class, () -> typeMetadata.convert("10,15"));
    }
}
