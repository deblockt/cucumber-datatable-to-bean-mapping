package com.deblock.cucumber.datatable.mapper.typemetadata.collections;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionListTypeMetadataTest {

    @Test
    public void shouldReturnTypeMetadataSample() {
        final var typeMetadata = new CollectionLikeTypeMetadata(
                buildTypeMetadata("string", "value"),
                Collectors.toList(),
                "list"
        );

        assertThat(typeMetadata.typeDescription()).isEqualTo("list<string>");
        assertThat(typeMetadata.sample()).isEqualTo("value,value");
    }

    @Test
    public void shouldUseTypeMetadataAndListCollectorToBuildValues() {
        final var typeMetadata = new CollectionLikeTypeMetadata(
                buildTypeMetadata("string", "value", it -> it + "foo"),
                Collectors.toList(),
                "list"
        );

        final var result = typeMetadata.convert("a,b,c");

        assertThat(result).isEqualTo(List.of("afoo", "bfoo", "cfoo"));
    }

    @Test
    public void shouldUseTypeMetadataAndSetCollectorToBuildValues() {
        final var typeMetadata = new CollectionLikeTypeMetadata(
                buildTypeMetadata("string", "value", String::toUpperCase),
                Collectors.toSet(),
                "set"
        );

        final var result = typeMetadata.convert("a,b,c");

        assertThat(result).isEqualTo(Set.of("A", "B", "C"));
    }

    @Test
    public void shouldTrimValues() {
        final var typeMetadata = new CollectionLikeTypeMetadata(
                buildTypeMetadata("string", "value"),
                Collectors.toSet(),
                "set"
        );

        final var result = typeMetadata.convert("a, b, c");

        assertThat(result).isEqualTo(Set.of("a", "b", "c"));
    }

    public TypeMetadata buildTypeMetadata(String description, String sample) {
        return buildTypeMetadata(description, sample, it -> it);
    }

    public TypeMetadata buildTypeMetadata(String description, String sample, Function<String, Object> mapper) {
        return new TypeMetadata() {
            @Override
            public String typeDescription() {
                return description;
            }

            @Override
            public String sample() {
                return sample;
            }

            @Override
            public Object convert(String value) throws ConversionError {
                return mapper.apply(value);
            }
        };
    }
}
