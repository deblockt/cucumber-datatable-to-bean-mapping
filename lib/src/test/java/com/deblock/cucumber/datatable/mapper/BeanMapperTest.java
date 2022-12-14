package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.beans.MalformedBeanPrivateColumnWithPrivateSetter;
import com.deblock.cucumber.datatable.mapper.beans.MalformedBeanPrivateColumnWithoutSetter;
import com.deblock.cucumber.datatable.mapper.beans.MalformedBeanWithPrivateConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanMapperTest {

    @Test
    public void shouldReadMetadataFromColumnAnnotatedColumns() {
        final var beanMapper = new BeanMapper(Bean.class, new MockMetadataFactory());

        final var result = beanMapper.headers();

        List<DatatableHeader> expectedHeaders = List.of(
                new DatatableHeader(List.of("stringProp"), "", false, null, null),
                new DatatableHeader(List.of("intProp"), "a property with primitive int", true, "10", null),
                new DatatableHeader(List.of("other bean", "my bean"), "", true, null, null),
                new DatatableHeader(List.of("private list"), "", false, null, null),
                new DatatableHeader(List.of("mandatory with default value"), "", true, "default", null)
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("typeMetadata")
                .isEqualTo(expectedHeaders);
    }

    @Test
    public void shouldReturnErrorIfPrivateFieldHasNoSetter() {
        final var malformedBeanException = Assertions.assertThrows(
                MalformedBeanException.class,
                () -> new BeanMapper(MalformedBeanPrivateColumnWithoutSetter.class, new MockMetadataFactory())
        );

        assertThat(malformedBeanException.getMessage())
                .isEqualTo("Class MalformedBeanPrivateColumnWithoutSetter: the field intProp should be public or have a setter setIntProp");
    }

    @Test
    public void shouldReturnErrorIfPrivateFieldHasPrivateSetter() {
        final var malformedBeanException = Assertions.assertThrows(
                MalformedBeanException.class,
                () -> new BeanMapper(MalformedBeanPrivateColumnWithPrivateSetter.class, new MockMetadataFactory())
        );

        assertThat(malformedBeanException.getMessage())
                .isEqualTo("Class MalformedBeanPrivateColumnWithPrivateSetter: the field intProp should be public or have a setter setIntProp");
    }

    @Test
    public void shouldReturnErrorIfThereIsNoPublicConstructor() {
        final var beanMapper = new BeanMapper(MalformedBeanWithPrivateConstructor.class, new MockMetadataFactory());

        final var malformedBeanException = Assertions.assertThrows(
                MalformedBeanException.class,
                () -> beanMapper.convert(Map.of())
        );

        assertThat(malformedBeanException.getMessage())
                .isEqualTo("Class MalformedBeanWithPrivateConstructor: should have a public constructor without parameter");
    }

    @Test
    public void shouldMapDataToBean() {
        final var beanMapper = new BeanMapper(Bean.class, new MockMetadataFactory());

        final var result = (Bean) beanMapper.convert(Map.of(
                "stringProp", "string",
                "intProp", "101",
                "private list", "10,11"
        ));

        assertThat(result.prop).isEqualTo("string");
        assertThat(result.intProp).isEqualTo(101);
        assertThat(result.getPrivateList()).isEqualTo(List.of("10", "11"));
    }

    @Test
    public void shouldSetDefaultValueIfColumnIsNotSet() {
        final var beanMapper = new BeanMapper(Bean.class, new MockMetadataFactory());

        final var result = (Bean) beanMapper.convert(Map.of(
                "stringProp", "string",
                "private list", "10,11"
        ));

        assertThat(result.prop).isEqualTo("string");
        assertThat(result.intProp).isEqualTo(10);
        assertThat(result.getPrivateList()).isEqualTo(List.of("10", "11"));
    }


    public static class MockMetadataFactory implements TypeMetadataFactory {

        @Override
        public TypeMetadata build(Type type) {
            return new TypeMetadata() {
                @Override
                public String typeDescription() {
                    return null;
                }

                @Override
                public String sample() {
                    return null;
                }

                @Override
                public Object convert(String value) throws ConversionError {
                    if (type.equals(String.class)) {
                        return value;
                    } else if (type.equals(Integer.class) || type.equals(int.class)) {
                        return Integer.parseInt(value);
                    } else if (type instanceof ParameterizedType ptype && ptype.getRawType().equals(List.class)) {
                        return List.of(value.split(","));
                    }
                    throw new IllegalArgumentException("can not convert type " + type);
                }
            };
        }
    }
}
