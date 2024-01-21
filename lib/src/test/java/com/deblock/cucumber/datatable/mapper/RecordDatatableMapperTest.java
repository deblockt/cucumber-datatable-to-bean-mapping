package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.beans.Record;
import com.deblock.cucumber.datatable.mapper.beans.RecordWithNestedRecord;
import com.deblock.cucumber.datatable.mapper.datatable.RecordDatatableMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordDatatableMapperTest {

    @Test
    public void shouldReadMetadataFromColumnAnnotatedColumns() {
        final var beanMapper = new RecordDatatableMapper(Record.class, new GenericMapperFactory(new MockMetadataFactory()));

        final var result = beanMapper.headers();

        List<DatatableHeader> expectedHeaders = List.of(
                new DatatableHeader(List.of("stringProp"), "", false, null, null),
                new DatatableHeader(List.of("intProp"), "a property with primitive int", true, "10", null),
                new DatatableHeader(List.of("other bean", "my bean"), "", true, null, null),
                new DatatableHeader(List.of("list"), "", false, null, null),
                new DatatableHeader(List.of("field with default name", "fieldWithDefaultName"), "", false, null, null),
                new DatatableHeader(List.of("mandatory with default value"), "", true, "default", null)
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("typeMetadata")
                .isEqualTo(expectedHeaders);
    }

    @Test
    public void shouldReadMetadataFromColumnAnnotatedColumnsOnNestedObjects() {
        final var beanMapper = new RecordDatatableMapper(RecordWithNestedRecord.class, new GenericMapperFactory(new MockMetadataFactory()));

        final var result = beanMapper.headers();

        List<DatatableHeader> expectedHeaders = List.of(
            new DatatableHeader(List.of("column"), "", false, null, null),
            new DatatableHeader(List.of("column1", "column 1"), "the column1. the column1 on second object", false, null, null),
            new DatatableHeader(List.of("column2"), "", false, null, null),
            new DatatableHeader(List.of("column3"), "", true, null, null),
            new DatatableHeader(List.of("column4"), "", true, null, null)
        );
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("typeMetadata")
                .ignoringCollectionOrder()
                .isEqualTo(expectedHeaders);
    }


    @Test
    public void shouldMapDataToBean() {
        final var beanMapper = new RecordDatatableMapper(Record.class, new GenericMapperFactory(new MockMetadataFactory()));

        final var result = (Record) beanMapper.convert(Map.of(
                "stringProp", "string",
                "intProp", "101",
                "list", "10,11",
                "field with default name", "test"
        ));

        assertThat(result.prop()).isEqualTo("string");
        assertThat(result.fieldWithDefaultName()).isEqualTo("test");
        assertThat(result.intProp()).isEqualTo(101);
        assertThat(result.list()).isEqualTo(List.of("10", "11"));
        assertThat(result.otherBean()).isNull();
        assertThat(result.nonAnnotatedColumn()).isNull();
    }

    @Test
    public void shouldMapDataToBeanWithNestedObjectWithAllRequiredObjectFilled() {
        final var beanMapper = new RecordDatatableMapper(RecordWithNestedRecord.class, new GenericMapperFactory(new MockMetadataFactory()));

        final var result = (RecordWithNestedRecord) beanMapper.convert(Map.of(
                "column", "value",
                "column1", "value1",
                "column2", "value2",
                "column3", "value3"
        ));

        assertThat(result.column()).isEqualTo("value");
        assertThat(result.nestedObjectAllMandatory()).isEqualTo(new RecordWithNestedRecord.NestedObject("value1", "value2"));
        assertThat(result.nestedObjectWithOptional()).isEqualTo(new RecordWithNestedRecord.NestedObject2("value1", "value3"));
    }

    @Test
    public void shouldSetNestedObjectToNullInCaseOfMissingFields() {
        final var beanMapper = new RecordDatatableMapper(RecordWithNestedRecord.class, new GenericMapperFactory(new MockMetadataFactory()));

        final var result = (RecordWithNestedRecord) beanMapper.convert(Map.of(
                "column", "value",
                "column1", "value1",
                "column2", "value2",
                "column3", "value3"
        ));

        assertThat(result.column()).isEqualTo("value");
        assertThat(result.nestedObjectAllMandatory()).isEqualTo(new RecordWithNestedRecord.NestedObject("value1", "value2"));
        assertThat(result.nestedObjectWithOptional()).isEqualTo(new RecordWithNestedRecord.NestedObject2("value1", "value3"));
        assertThat(result.optionalNestedObjectAllMandatory()).isNull();
    }

    @Test
    public void shouldUseDefaultValueWhenColumnIsNotPresent() {
        final var beanMapper = new RecordDatatableMapper(Record.class, new GenericMapperFactory(new MockMetadataFactory()));

        final var result = (Record) beanMapper.convert(Map.of(
                "stringProp", "string",
                "list", "10,11"
        ));

        assertThat(result.prop()).isEqualTo("string");
        assertThat(result.intProp()).isEqualTo(10);
        assertThat(result.list()).isEqualTo(List.of("10", "11"));
        assertThat(result.otherBean()).isNull();
        assertThat(result.fieldWithDefaultName()).isNull();
        assertThat(result.nonAnnotatedColumn()).isNull();
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
                    throw new IllegalArgumentException("ca not convert type " + type);
                }
            };
        }
    }
}
