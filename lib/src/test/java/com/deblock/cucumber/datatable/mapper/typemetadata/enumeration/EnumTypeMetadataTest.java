package com.deblock.cucumber.datatable.mapper.typemetadata.enumeration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumTypeMetadataTest {

    @Test
    public void typeDescriptionShouldReturnAllPossibleValues() {
        final var typeMetadata = new EnumTypeMetadata(TestEnum.class);

        final var result = typeMetadata.typeDescription();

        assertThat(result).isEqualTo("TestEnum: VALUE1, VALUE2");
    }

    @Test
    public void sampleShouldReturnTheFirstEnumValue() {
        final var typeMetadata = new EnumTypeMetadata(TestEnum.class);

        final var result = typeMetadata.sample();

        assertThat(result).isEqualTo("VALUE1");
    }

    @Test
    public void convertShouldReturnTheEnumValueIfExists() {
        final var typeMetadata = new EnumTypeMetadata(TestEnum.class);

        final var result = typeMetadata.convert("VALUE2");

        assertThat(result).isEqualTo(TestEnum.VALUE2);
    }

    @Test
    public void convertShouldReturnExceptionIfEnumHasNoValues() {
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new EnumTypeMetadata(EmptyEnum.class));

        assertThat(exception.getMessage()).isEqualTo("enum EmptyEnum should have at least one value");
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }

    private enum EmptyEnum {

    }
}
