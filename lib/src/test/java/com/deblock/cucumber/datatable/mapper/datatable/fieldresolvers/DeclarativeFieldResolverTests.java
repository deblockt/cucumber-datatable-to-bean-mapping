package com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeclarativeFieldResolverTests {

    @Nested
    public class Bean {
        @Test
        public void shouldReturnAllColumnInfo() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new DeclarativeFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldColumnAnnotated.class.getField("field"), BeanWithPublicFieldColumnAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("first field", "field 1"),
                    true,
                    "test description",
                    "test"
            ));
        }

        @Test
        public void shouldReturnEmptyIfFieldIsNotAnnotatedWithColumn() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new DeclarativeFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldNotAnnotated.class.getField("field"), BeanWithPublicFieldNotAnnotated.class);

            assertThat(result).isEmpty();
        }

        @Test
        public void shouldBuildInfoWhenColumnAnnotationHasNaParams() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("fieldWithDefaultValues")).thenReturn(List.of("mock name"));
            final var fieldResolver = new DeclarativeFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldColumnAnnotated.class.getField("fieldWithDefaultValues"), BeanWithPublicFieldColumnAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    false,
                    "",
                    null
            ));
        }
    }

    @Nested
    public class Record {
        @Test
        public void shouldReturnAllColumnInfo() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new DeclarativeFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldColumnAnnotated.class.getRecordComponents()[0], RecordWithPublicFieldColumnAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("first field", "field 1"),
                    true,
                    "test description",
                    "test"
            ));
        }

        @Test
        public void shouldReturnEmptyIfFieldIsNotAnnotatedWithColumn() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new DeclarativeFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldNotAnnotated.class.getRecordComponents()[0], RecordWithPublicFieldNotAnnotated.class);

            assertThat(result).isEmpty();
        }

        @Test
        public void shouldBuildInfoWhenColumnAnnotationHasNaParams() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("fieldWithDefaultValues")).thenReturn(List.of("mock name"));
            final var fieldResolver = new DeclarativeFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldColumnAnnotated.class.getRecordComponents()[1], RecordWithPublicFieldColumnAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    false,
                    "",
                    null
            ));
        }
    }

    class BeanWithPublicFieldColumnAnnotated {
        @Column(value = {"first field", "field 1"}, description = "test description", mandatory = false, defaultValue = "test")
        public String field;
        @Column
        public String fieldWithDefaultValues;
    }

    class BeanWithPublicFieldNotAnnotated {
        public String field;
    }

    record RecordWithPublicFieldColumnAnnotated(
        @Column(value = {"first field", "field 1"}, description = "test description", mandatory = false, defaultValue = "test")
        String field,
        @Column
        String fieldWithDefaultValues
    ) {}

    record RecordWithPublicFieldNotAnnotated(
        String field
    ) {}
}
