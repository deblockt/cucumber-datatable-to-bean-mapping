package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CustomTypeMetadataFactoryTest {

    @Test
    public void shouldAllowToRegisterCustomType() {
        final var typeMetadata = Mockito.mock(TypeMetadata.class);
        CustomTypeMetadataFactory.addCustomType(String.class, typeMetadata);

        final var getTypeMetadata = CustomTypeMetadataFactory.INSTANCE.build(String.class);

        Assertions.assertThat(getTypeMetadata).isEqualTo(typeMetadata);
    }

    @Test
    public void shouldReturnNullIfTypeIsNotRegistered() {
        CustomTypeMetadataFactory.addCustomType(String.class, Mockito.mock(TypeMetadata.class));

        final var getTypeMetadata = CustomTypeMetadataFactory.INSTANCE.build(Integer.class);

        Assertions.assertThat(getTypeMetadata).isNull();
    }
}
