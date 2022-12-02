package com.deblock.cucumber.datatable.mapper.typemetadata.enumeration;

import com.deblock.cucumber.datatable.mapper.beans.Bean;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumTypeMetadataFactoryTest {

    @Test
    public void shouldReturnNullIfClassIsNotAnEnum() {
        final var metadataFactory = new EnumTypeMetadataFactory();

        final var result = metadataFactory.build(Bean.class);

        assertThat(result).isNull();
    }

    @Test
    public void shouldReturnAnEnumMetadataForEnumType() {
        final var metadataFactory = new EnumTypeMetadataFactory();

        final var result = metadataFactory.build(MyEnum.class);

        assertThat(result).isInstanceOf(EnumTypeMetadata.class);
    }

    private enum MyEnum {
        VALUE
    }
}
