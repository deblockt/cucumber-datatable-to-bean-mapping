package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumericTypeMetadataTest {

    @Test
    public void shouldReturnABigIntegerFromInput() {
        final var typeMetadata = new NumericTypeMetadata("bigInteger", "10", BigInteger::new);

        final var result = typeMetadata.convert("14");

        assertThat(result).isEqualTo(new BigInteger("14"));
    }

    @Test
    public void shouldReturnADoubleForNumberWithDotSeparator() {
        final var typeMetadata = new NumericTypeMetadata("double", "10.10", Double::parseDouble);

        final var result = typeMetadata.convert("14.16");

        assertThat(result).isEqualTo(14.16d);
    }

    @Test
    public void shouldReturnADoubleForNumberWithCommaSeparator() {
        final var typeMetadata = new NumericTypeMetadata("double", "10.10", Double::parseDouble);

        final var result = typeMetadata.convert("14,16");

        assertThat(result).isEqualTo(14.16d);
    }

    @Test
    public void shouldReturnConversionErrorForInvalidInput() {
        final var typeMetadata = new NumericTypeMetadata("int", "10.10", Integer::parseInt);

        final var exception = assertThrows(TypeMetadata.ConversionError.class, () -> typeMetadata.convert("foo"));

        assertThat(exception).hasMessage("\"foo\" is a invalid format for int");
    }
}
