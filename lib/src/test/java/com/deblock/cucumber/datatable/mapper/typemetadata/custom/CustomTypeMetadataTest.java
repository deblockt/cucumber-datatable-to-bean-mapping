package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.beans.custom.CustomBean;
import com.deblock.cucumber.datatable.mapper.beans.custom.CustomBean2;
import io.cucumber.core.backend.Lookup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;


class CustomTypeMetadataTest {
    Lookup lookup = Mockito.mock(Lookup.class);

    @Test
    void shouldReturnAnnotationTypeDescriptionAsDescription() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class), lookup);

        final var result = typeMetadata.typeDescription();

        assertThat(result).isEqualTo("CustomBean");
    }

    @Test
    void shouldReturnAnnotationSampleAsSample() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class), lookup);

        final var result = typeMetadata.sample();

        assertThat(result).isEqualTo("aString");
    }

    @Test
    void shouldUseTheMethodToMapString() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class), lookup);

        final var result = typeMetadata.convert("aValue");

        assertThat(result).isInstanceOf(CustomBean.class);
        assertThat(((CustomBean)result).value()).isEqualTo("aValue");
    }

    @Test
    void shouldUseNonStaticMethod() throws NoSuchMethodException {
        Mockito.when(lookup.getInstance(CustomBean.class)).thenReturn(new CustomBean(""));
        final var method = CustomBean.class.getMethod("nonStaticMapperForTest", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class), lookup);

        final var result = typeMetadata.convert("aValue");

        assertThat(result).isInstanceOf(CustomBean.class);
        assertThat(((CustomBean)result).value()).isEqualTo("aValue");
    }

    @Test
    void shouldThrowConversionExceptionIfMapperFail() throws NoSuchMethodException {
        final var method = CustomBean2.class.getMethod("mapperThrowingException", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class), lookup);

        final var exception = Assertions.assertThrows(TypeMetadata.ConversionError.class, () -> typeMetadata.convert("aValue"));

        assertThat(exception.getMessage()).isEqualTo("method CustomBean2.mapperThrowingException has throw the error: error aValue is not supported");
        assertThat(exception.getCause()).isNotNull();
    }

    @Test
    void shouldFailIfAnnotationIsNull() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null, lookup));

        assertThat(exception.getMessage()).isEqualTo("The annotation should not be null");
    }

    @Test
    void shouldFailIfMethodHasNoParameter() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapperWithoutParameter");
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null, lookup));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.mapperWithoutParameter should have one String parameter to be used with annotation CustomDatatableFieldMapper");

    }

    @Test
    void shouldFailIfMethodHasMoreThanOneParameter() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapperWithTwoParametersParameter", String.class, String.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null, lookup));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.mapperWithTwoParametersParameter should have one String parameter to be used with annotation CustomDatatableFieldMapper");
    }

    @Test
    void shouldFailIfMethodHasANonStringParameter() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromNonString", Integer.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null, lookup));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.mapFromNonString should have one String parameter to be used with annotation CustomDatatableFieldMapper");
    }
}
