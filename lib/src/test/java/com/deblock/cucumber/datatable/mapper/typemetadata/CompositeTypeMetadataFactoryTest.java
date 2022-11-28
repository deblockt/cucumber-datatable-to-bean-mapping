package com.deblock.cucumber.datatable.mapper.typemetadata;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompositeTypeMetadataFactoryTest {

    @Test
    public void shouldReturnFirstNonNullTypeMetadata() {
        final var type = String.class;
        final var nullFactory = Mockito.mock(TypeMetadataFactory.class);
        Mockito.when(nullFactory.build(type)).thenReturn(null);
        final var nonNullFactory = Mockito.mock(TypeMetadataFactory.class);
        final var typeMetadata = Mockito.mock(TypeMetadata.class);
        Mockito.when(nonNullFactory.build(type)).thenReturn(typeMetadata);
        final var typeMetadataFactory = new CompositeTypeMetadataFactory(nullFactory, nonNullFactory);

        final var result = typeMetadataFactory.build(type);

        assertThat(result).isEqualTo(typeMetadata);
    }

    @Test
    public void shouldThrowExceptionIfNoFactoryFound() {
        final var type = String.class;
        final var nullFactory = Mockito.mock(TypeMetadataFactory.class);
        Mockito.when(nullFactory.build(type)).thenReturn(null);
        final var nonNullFactory = Mockito.mock(TypeMetadataFactory.class);
        final var typeMetadata = Mockito.mock(TypeMetadata.class);
        Mockito.when(nonNullFactory.build(type)).thenReturn(typeMetadata);
        final var typeMetadataFactory = new CompositeTypeMetadataFactory(nullFactory, nonNullFactory);

        final var exception = assertThrows(IllegalArgumentException.class, () -> typeMetadataFactory.build(Integer.class));

        assertThat(exception.getMessage()).isEqualTo("can not find any converter for class class java.lang.Integer. You can define your own converter using CustomTypeMetadataFactory.addCustomType");
    }

    @Test
    public void factoryCanBeAddedUsingAddMethod() {
        final var type = String.class;
        final var nullFactory = Mockito.mock(TypeMetadataFactory.class);
        Mockito.when(nullFactory.build(type)).thenReturn(null);
        final var nonNullFactory = Mockito.mock(TypeMetadataFactory.class);
        final var typeMetadata = Mockito.mock(TypeMetadata.class);
        Mockito.when(nonNullFactory.build(type)).thenReturn(typeMetadata);
        final var typeMetadataFactory = new CompositeTypeMetadataFactory(nullFactory);
        typeMetadataFactory.add(nonNullFactory);

        final var result = typeMetadataFactory.build(type);

        assertThat(result).isEqualTo(typeMetadata);
    }
}
