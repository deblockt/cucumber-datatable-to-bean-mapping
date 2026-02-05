package com.deblock.cucumber.datatable.mapper.typemetadata.map;

import com.deblock.cucumber.datatable.data.TypeMetadata.ConversionError;
import io.cucumber.messages.ndjson.internal.com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapTypeMetadataTest {

    @Test
    void shouldConvertAJsonObjectToAMap() {
        final var metadata = new MapTypeMetadata(new TypeReference<Map<String, String>>(){}.getType());

        final var result = metadata.convert(
                """
                {"foo": "bar"}
                """
        );

        assertThat(result).isEqualTo(Map.of("foo", "bar"));
    }

    @Test
    void shouldReturnErrorWhenJsonMalformed() {
        final var metadata = new MapTypeMetadata(new TypeReference<Map<String, String>>(){}.getType());

        final var exception = assertThrows(ConversionError.class, () -> metadata.convert(
                """
                {"foo": }
                """
        ));

        assertThat(exception).hasMessage("Unexpected character ('}' (code 125)): expected a valid value (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n" +
                " at [Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 9]");
    }
}
