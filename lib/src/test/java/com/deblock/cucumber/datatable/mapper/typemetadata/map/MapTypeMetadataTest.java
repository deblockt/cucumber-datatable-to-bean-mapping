package com.deblock.cucumber.datatable.mapper.typemetadata.map;

import com.deblock.cucumber.datatable.data.TypeMetadata.ConversionError;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapTypeMetadataTest {

    @Test
    void shouldConvertAJsonObjectToAMap() {
        final var metadata = new MapTypeMetadata(new TypeToken<Map<String, String>>(){}.getType());

        final var result = metadata.convert(
                """
                {"foo": "bar"}
                """
        );

        assertThat(result).isEqualTo(Map.of("foo", "bar"));
    }

    @Test
    void shouldReturnErrorWhenJsonMalformed() {
        final var metadata = new MapTypeMetadata(new TypeToken<Map<String, String>>(){}.getType());

        final var exception = assertThrows(ConversionError.class, () -> metadata.convert(
                """
                {"foo": }
                """
        ));

        assertThat(exception).hasMessage("Expected value at line 1 column 9 path $.\n" +
                "See https://github.com/google/gson/blob/main/Troubleshooting.md#malformed-json");
    }

    @Test
    void shouldConvertJsonValuesToTheMapGenericType() {
        final var metadata = new MapTypeMetadata(new TypeToken<Map<String, List<Integer>>>(){}.getType());

        final var result = metadata.convert(
                """
                {"foo": [1, 2], "bar": []}
                """
        );

        assertThat(result).isEqualTo(Map.of("foo", List.of(1, 2), "bar", List.of()));
    }
}
