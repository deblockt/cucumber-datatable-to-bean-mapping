package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    public void shouldReadCommaAsDecimalSeparatorNotAsThousandsSeparator() {
        final var doubleTypeMetadata = new NumericTypeMetadata("double", "10.10", Double::parseDouble);
        final var bigDecimalTypeMetadata = new NumericTypeMetadata("bigDecimal", "10.10", BigDecimal::new);

        assertThat(doubleTypeMetadata.convert("1,000")).isEqualTo(1.0d);
        assertThat(bigDecimalTypeMetadata.convert("1,000")).isEqualTo(new BigDecimal("1.000"));
    }

    @Test
    public void shouldReturnConversionErrorForIntegerWithThousandsSeparator() {
        final var typeMetadata = new NumericTypeMetadata("int", "10", Integer::parseInt);

        final var exception = assertThrows(TypeMetadata.ConversionError.class, () -> typeMetadata.convert("1,000"));

        assertThat(exception).hasMessage("invalid format for int");
    }

    @Test
    public void shouldReturnConversionErrorForInvalidInput() {
        final var typeMetadata = new NumericTypeMetadata("int", "10.10", Integer::parseInt);

        final var exception = assertThrows(TypeMetadata.ConversionError.class, () -> typeMetadata.convert("foo"));

        assertThat(exception).hasMessage("invalid format for int");
    }
}
