package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.GenericMapperFactory;
import com.deblock.cucumber.datatable.mapper.MalformedBeanException;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.beans.BeanWithNestedObjects;
import com.deblock.cucumber.datatable.mapper.beans.BeanWithNestedRecordNameMapping;
import com.deblock.cucumber.datatable.mapper.beans.BeanWithUnsupportedConverter;
import com.deblock.cucumber.datatable.mapper.beans.CustomBeanWithoutMapper;
import com.deblock.cucumber.datatable.mapper.beans.MalformedBeanPrivateColumnWithPrivateSetter;
import com.deblock.cucumber.datatable.mapper.beans.MalformedBeanPrivateColumnWithoutSetter;
import com.deblock.cucumber.datatable.mapper.beans.MalformedBeanWithPrivateConstructor;
import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver;
import com.deblock.cucumber.datatable.mapper.name.MultiNameColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.exceptions.NoConverterFound;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BeanMapperTest {

    @Test
    public void shouldReadMetadataFromColumnAnnotatedColumns() {
        final var beanMapper = new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(Bean.class);

        final var result = beanMapper.headers();

        List<DatatableHeader> expectedHeaders = List.of(
                new DatatableHeader(new ColumnName("stringProp"), "", false, null, null),
                new DatatableHeader(new ColumnName("intProp"), "a property with primitive int", true, "10", null),
                new DatatableHeader(new ColumnName("other bean", "my bean"), "", true, null, null),
                new DatatableHeader(new ColumnName("private list"), "", false, null, null),
                new DatatableHeader(new ColumnName("mandatory with default value"), "", true, "default", null),
                new DatatableHeader(new ColumnName("field with default name", "fieldWithDefaultName"), "", false, null, null)
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("typeMetadata")
                .isEqualTo(expectedHeaders);
    }

    @Test
    public void shouldReadMetadataFromColumnAnnotatedColumnsOnNestedObjects() {
        final var beanMapper = new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(BeanWithNestedObjects.class);

        final var result = beanMapper.headers();

        List<DatatableHeader> expectedHeaders = List.of(
                new DatatableHeader(new ColumnName("column"), "", false, null, null),
                new DatatableHeader(new ColumnName("column1", "column 1"), "the column1. the column1 on second object", false, null, null),
                new DatatableHeader(new ColumnName("column2"), "", false, null, null),
                new DatatableHeader(new ColumnName("column3"), "", true, null, null),
                new DatatableHeader(new ColumnName("column4"), "", true, null, null),
                new DatatableHeader(new ColumnName("nestedWithCustomMapper"), "", false, null, null)
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("typeMetadata")
                .ignoringCollectionOrder()
                .isEqualTo(expectedHeaders);
    }

    @Test
    public void shouldReplaceParentName() {
        final var beanMapper = new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(BeanWithNestedRecordNameMapping.class);

        final var result = beanMapper.headers();

        List<DatatableHeader> expectedHeaders = List.of(
                new DatatableHeader(new ColumnName("nested object1 column1", "nestedObject1 column1"), "the column1", false, null, null),
                new DatatableHeader(new ColumnName("nested object1_2 column", "nestedObject1_2 column"), "", false, null, null),
                new DatatableHeader(new ColumnName("object2 column1"), "the column1", false, null, null),
                new DatatableHeader(new ColumnName("object2_2 column"), "", false, null, null)
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("typeMetadata")
                .ignoringCollectionOrder()
                .isEqualTo(expectedHeaders);
    }

    @Test
    public void shouldReturnErrorIfPrivateFieldHasNoSetter() {
        final var malformedBeanException = assertThrows(
                MalformedBeanException.class,
                () -> new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(MalformedBeanPrivateColumnWithoutSetter.class)
        );

        assertThat(malformedBeanException.getMessage())
                .isEqualTo("Class MalformedBeanPrivateColumnWithoutSetter: the field intProp should be public or have a setter setIntProp");
    }

    @Test
    public void shouldReturnErrorIfPrivateFieldHasPrivateSetter() {
        final var malformedBeanException = assertThrows(
                MalformedBeanException.class,
                () -> new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(MalformedBeanPrivateColumnWithPrivateSetter.class)
        );

        assertThat(malformedBeanException.getMessage())
                .isEqualTo("Class MalformedBeanPrivateColumnWithPrivateSetter: the field intProp should be public or have a setter setIntProp");
    }

    @Test
    public void shouldReturnErrorIfThereIsNoPublicConstructor() {
        final var malformedBeanException = assertThrows(
                MalformedBeanException.class,
                () -> new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                    .build(MalformedBeanWithPrivateConstructor.class)
        );

        assertThat(malformedBeanException.getMessage())
                .isEqualTo("Class MalformedBeanWithPrivateConstructor: should have a public constructor without parameter");
    }

    @Test
    public void shouldMapDataToBean() {
        final var beanMapper = new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver()).build(Bean.class);

        final var result = (Bean) beanMapper.convert(Map.of(
                "stringProp", "string",
                "intProp", "101",
                "private list", "10,11",
                "field with default name", "test"
        ));

        assertThat(result.prop).isEqualTo("string");
        assertThat(result.fieldWithDefaultName).isEqualTo("test");
        assertThat(result.intProp).isEqualTo(101);
        assertThat(result.getPrivateList()).isEqualTo(List.of("10", "11"));
    }

    @Test
    public void shouldSetDefaultValueIfColumnIsNotSet() {
        final var beanMapper = new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(Bean.class);

        final var result = (Bean) beanMapper.convert(Map.of(
                "stringProp", "string",
                "private list", "10,11"
        ));

        assertThat(result.prop).isEqualTo("string");
        assertThat(result.intProp).isEqualTo(10);
        assertThat(result.fieldWithDefaultName).isEqualTo(null);
        assertThat(result.getPrivateList()).isEqualTo(List.of("10", "11"));
    }

    @Test
    public void shouldMapDataToBeanWithNestedObjectWithAllRequiredObjectFilled() {
        final var beanMapper = new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(BeanWithNestedObjects.class);

        final var result = (BeanWithNestedObjects) beanMapper.convert(Map.of(
                "column", "value",
                "column1", "value1",
                "column2", "value2",
                "column3", "value3",
                "nestedWithCustomMapper", "nestedWithCustomMapperValue"
        ));

        assertThat(result.column).isEqualTo("value");
        assertThat(result.nestedObjectAllMandatory).isEqualTo(new BeanWithNestedObjects.NestedObject("value1", "value2"));
        assertThat(result.getNestedObjectWithOptional()).isEqualTo(new BeanWithNestedObjects.NestedObject2("value1", "value3"));
        assertThat(result.nestedWithCustomMapper.ignoredColumn).isEqualTo("nestedWithCustomMapperValue");
    }

    @Test
    public void shouldThrowErrorIfAnnotatedColumnFieldTypeNotSupported() {
        final var result = assertThrows(
                NoConverterFound.class,
                () -> new GenericMapperFactory(new BeanMapperTest.MockMetadataFactory(), defaultFieldResolver())
                .build(BeanWithUnsupportedConverter.class)
        );

        assertThat(result).hasMessage("can not find any converter for class class com.deblock.cucumber.datatable.mapper.beans.CustomBeanWithoutMapper. You can define your own converter using @CustomDatatableFieldMapper");
    }

    public FieldResolver defaultFieldResolver() {
        final var fieldResolver = new DeclarativeFieldResolver();
        fieldResolver.configure(new MultiNameColumnNameBuilder());
        return fieldResolver;
    }

    public static class MockMetadataFactory implements TypeMetadataFactory {

        @Override
        public TypeMetadata build(Type type) {
            if (BeanWithNestedObjects.NestedWithCustomMapper.class.equals(type)) {
                return new TypeMetadata() {
                    @Override
                    public String typeDescription() {
                        return "NestedWithCustomMapper";
                    }

                    @Override
                    public String sample() {
                        return "1111";
                    }

                    @Override
                    public Object convert(String value) throws ConversionError {
                        return new BeanWithNestedObjects.NestedWithCustomMapper(value);
                    }
                };
            }

            if ((type instanceof Class<?> clazz && clazz.isAnnotationPresent(DataTableWithHeader.class) || CustomBeanWithoutMapper.class.equals(type))) {
                throw new NoConverterFound(type);
            }

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
