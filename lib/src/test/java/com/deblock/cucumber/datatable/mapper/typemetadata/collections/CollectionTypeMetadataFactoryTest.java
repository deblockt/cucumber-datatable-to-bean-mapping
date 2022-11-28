package com.deblock.cucumber.datatable.mapper.typemetadata.collections;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionTypeMetadataFactoryTest {
    public List<String> usedToGetListStringType;
    public Set<Integer> usedToGetSetIntegerType;
    public Collection<Integer> usedToGetCollectionIntegerType;

    @Test
    public void shouldReturnListForList() throws NoSuchFieldException {
        final var typeMetadataFactory = new CollectionTypeMetadataFactory(
                buildTypeMetadataFactory(String.class)
        );

        final var resultTypeMetadata = typeMetadataFactory.build(CollectionTypeMetadataFactoryTest.class.getField("usedToGetListStringType").getGenericType());
        final var converted = resultTypeMetadata.convert("a");

        assertThat(converted).isInstanceOf(List.class);
    }

    @Test
    public void shouldReturnListForCollection() throws NoSuchFieldException {
        final var typeMetadataFactory = new CollectionTypeMetadataFactory(
                buildTypeMetadataFactory(Integer.class)
        );

        final var resultTypeMetadata = typeMetadataFactory.build(CollectionTypeMetadataFactoryTest.class.getField("usedToGetCollectionIntegerType").getGenericType());
        final var converted = resultTypeMetadata.convert("a");

        assertThat(converted).isInstanceOf(List.class);
    }

    @Test
    public void shouldReturnTypeMetadataSample() throws NoSuchFieldException {
        final var typeMetadataFactory = new CollectionTypeMetadataFactory(
                buildTypeMetadataFactory(Integer.class)
        );

        final var resultTypeMetadata = typeMetadataFactory.build(CollectionTypeMetadataFactoryTest.class.getField("usedToGetSetIntegerType").getGenericType());
        final var converted = resultTypeMetadata.convert("a");

        assertThat(converted).isInstanceOf(Set.class);
    }

    public TypeMetadataFactory buildTypeMetadataFactory(Class<?> supportedType) {
        return new TypeMetadataFactory() {
            @Override
            public TypeMetadata build(Type type) {
                if (!type.equals(supportedType)) {
                    throw new IllegalArgumentException("expected " + supportedType + " but receive " + type);
                }
                return new TypeMetadata() {
                    @Override
                    public String typeDescription() {
                        return "unused";
                    }

                    @Override
                    public String sample() {
                        return "unused";
                    }

                    @Override
                    public Object convert(String value) throws ConversionError {
                        return value;
                    }
                };
            };
        };
    }
}
