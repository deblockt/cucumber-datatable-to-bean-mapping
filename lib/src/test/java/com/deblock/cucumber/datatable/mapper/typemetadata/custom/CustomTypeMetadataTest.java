package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.beans.custom.CustomBean;
import com.deblock.cucumber.datatable.mapper.beans.custom.CustomBean2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CustomTypeMetadataTest {

    @Test
    public void shouldReturnAnnotationTypeDescriptionAsDescription() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class));

        final var result = typeMetadata.typeDescription();

        assertThat(result).isEqualTo("CustomBean");
    }

    @Test
    public void shouldReturnAnnotationSampleAsSample() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class));

        final var result = typeMetadata.sample();

        assertThat(result).isEqualTo("aString");
    }

    @Test
    public void shouldUseTheMethodToMapString() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class));

        final var result = typeMetadata.convert("aValue");

        assertThat(result).isInstanceOf(CustomBean.class);
        assertThat(((CustomBean)result).value()).isEqualTo("aValue");
    }

    @Test
    public void shouldThrowConversionExceptionIfMapperFail() throws NoSuchMethodException {
        final var method = CustomBean2.class.getMethod("mapperThrowingException", String.class);
        final var typeMetadata = new CustomJavaBeanTypeMetadata(method, method.getAnnotation(CustomDatatableFieldMapper.class));

        final var exception = Assertions.assertThrows(TypeMetadata.ConversionError.class, () -> typeMetadata.convert("aValue"));

        assertThat(exception.getMessage()).isEqualTo("Unable to convert \"aValue\" using CustomBean2.mapperThrowingException to return CustomBean2");
        assertThat(exception.getCause()).isNotNull();
    }

    @Test
    public void shouldFailIfAnnotationIsNull() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromString", String.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null));

        assertThat(exception.getMessage()).isEqualTo("The annotation should not be null");
    }


    @Test
    public void shouldFailIfMethodIsNotStatic() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("nonStaticMapperForTest", String.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.nonStaticMapperForTest should be static to be used with annotation CustomDatatableFieldMapper");
    }

    @Test
    public void shouldFailIfMethodHasNoParameter() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapperWithoutParameter");
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.mapperWithoutParameter should have one String parameter to be used with annotation CustomDatatableFieldMapper");

    }

    @Test
    public void shouldFailIfMethodHasMoreThanOneParameter() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapperWithTwoParametersParameter", String.class, String.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.mapperWithTwoParametersParameter should have one String parameter to be used with annotation CustomDatatableFieldMapper");
    }

    @Test
    public void shouldFailIfMethodHasANonStringParameter() throws NoSuchMethodException {
        final var method = CustomBean.class.getMethod("mapFromNonString", Integer.class);
        final var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new CustomJavaBeanTypeMetadata(method, null));

        assertThat(exception.getMessage()).isEqualTo("The method CustomBean.mapFromNonString should have one String parameter to be used with annotation CustomDatatableFieldMapper");
    }
}
