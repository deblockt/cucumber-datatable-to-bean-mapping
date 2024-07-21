package com.deblock.cucumber.datatable.mapper.typemetadata.primitive;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UUIDTypeMetadataTest {

    @Test
    void shouldReturnTheUUID() {
        final var typeMetadata = new UUIDTypeMetadata();

        final var result = typeMetadata.convert("1610e6e2-d11f-4444-b6f3-1179089489ee");

        assertThat(result).isEqualTo(UUID.fromString("1610e6e2-d11f-4444-b6f3-1179089489ee"));
    }

    @Test
    void shouldReturnConversionErrorForInvalidInput() {
        final var typeMetadata = new UUIDTypeMetadata();

        final var exception = assertThrows(TypeMetadata.ConversionError.class, () -> typeMetadata.convert("aaaa"));

        assertThat(exception).hasMessage("invalid format for uuid");
    }

}
