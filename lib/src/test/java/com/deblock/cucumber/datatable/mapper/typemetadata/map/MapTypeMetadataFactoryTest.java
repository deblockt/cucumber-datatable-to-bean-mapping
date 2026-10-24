package com.deblock.cucumber.datatable.mapper.typemetadata.map;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapTypeMetadataFactoryTest {

    @Test
    void shouldConvertAJsonObjectToAMap() {
        final var metadataFactory = new MapTypeMetadataFactory();

        final var result = metadataFactory.build(new TypeToken<Map<String, String>>(){}.getType());

        assertThat(result).isInstanceOf(MapTypeMetadata.class);
    }

    @Test
    void shouldReturnErrorWhenJsonMalformed() {
        final var metadataFactory = new MapTypeMetadataFactory();

        final var result = metadataFactory.build(String.class);

        assertThat(result).isNull();
    }
}
