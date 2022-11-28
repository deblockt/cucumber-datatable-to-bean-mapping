package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTypeMetadataTest {

    @Test
    public void shouldReturnTheInput() {
        final var typeMetadata = new StringTypeMetadata();

        final var result = typeMetadata.convert("a super string");

        assertThat(result).isEqualTo("a super string");
    }

}
