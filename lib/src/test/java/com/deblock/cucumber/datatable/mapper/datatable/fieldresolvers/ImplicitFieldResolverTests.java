package com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.Ignore;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImplicitFieldResolverTests {

    @Nested
    public class Bean {
        @Test
        public void shouldReturnAllColumnInfoOnAnnotatedField() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
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
        public void shouldReturnEmptyIfColumnIsAnnotatedWithIgnoreAndColumn() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldColumnAnnotated.class.getField("fieldWithIgnoreAndColumn"), BeanWithPublicFieldColumnAnnotated.class);

            assertThat(result).isEmpty();
        }

        @Test
        public void shouldBuildInfoWhenColumnAnnotationHasNaParams() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("fieldWithDefaultValues")).thenReturn(List.of("mock name"));
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldColumnAnnotated.class.getField("fieldWithDefaultValues"), BeanWithPublicFieldColumnAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    false,
                    "",
                    null
            ));
        }

        @Test
        public void shouldReturnFieldInfoWhenFieldHasNoAnnotation() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("field")).thenReturn(List.of("mock name"));
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldNotAnnotated.class.getField("field"), BeanWithPublicFieldNotAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    true,
                    "",
                    null
            ));
        }

        @Test
        public void shouldReturnEmptyIfColumnIsAnnotatedWithIgnore() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(BeanWithPublicFieldNotAnnotated.class.getField("ignoreField"), BeanWithPublicFieldNotAnnotated.class);

            assertThat(result).isEmpty();
        }

        @Test
        public void shouldReturnFieldInfoIfNonAnnotatedFieldHasSetter() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("privateFieldWithSetter")).thenReturn(List.of("mock name"));
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);
            final var fields = BeanWithPublicFieldNotAnnotated.class.getDeclaredFields();
            final var result = fieldResolver.fieldInfo(fields[2], BeanWithPublicFieldNotAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    true,
                    "",
                    null
            ));
        }

        @Test
        public void shouldReturnEmptyIfNonAnnotatedFieldHasNoSetter() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);
            final var fields = BeanWithPublicFieldNotAnnotated.class.getDeclaredFields();
            final var result = fieldResolver.fieldInfo(fields[3], BeanWithPublicFieldNotAnnotated.class);

            assertThat(result).isEmpty();
        }

        @Test
        public void shouldReturnEmptyIfNonAnnotatedFieldHasMalformedSetter() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);
            final var fields = BeanWithPublicFieldNotAnnotated.class.getDeclaredFields();
            final var result = fieldResolver.fieldInfo(fields[4], BeanWithPublicFieldNotAnnotated.class);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    public class Record {
        @Test
        public void shouldReturnAllColumnInfo() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
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
        public void shouldReturnEmptyIfColumnIsAnnotatedWithIgnoreAndColumn() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldColumnAnnotated.class.getRecordComponents()[2], BeanWithPublicFieldColumnAnnotated.class);

            assertThat(result).isEmpty();
        }

        @Test
        public void shouldReturnFieldInfoIfItIsNotAnnotatedWithColumn() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("field")).thenReturn(List.of("mock name"));
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldNotAnnotated.class.getRecordComponents()[0], RecordWithPublicFieldNotAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    true,
                    "",
                    null
            ));
        }

        @Test
        public void shouldBuildInfoWhenColumnAnnotationHasNaParams() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            Mockito.when(columnNameBuilder.build("fieldWithDefaultValues")).thenReturn(List.of("mock name"));
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldColumnAnnotated.class.getRecordComponents()[1], RecordWithPublicFieldColumnAnnotated.class);

            assertThat(result).contains(new FieldResolver.FieldInfo(
                    new ColumnName("mock name"),
                    false,
                    "",
                    null
            ));
        }

        @Test
        public void shouldReturnEmptyIfColumnIsAnnotatedWithIgnore() throws NoSuchFieldException {
            final var columnNameBuilder = Mockito.mock(ColumnNameBuilder.class);
            final var fieldResolver = new ImplicitFieldResolver();
            fieldResolver.configure(columnNameBuilder);

            final var result = fieldResolver.fieldInfo(RecordWithPublicFieldNotAnnotated.class.getRecordComponents()[1], RecordWithPublicFieldNotAnnotated.class);

            assertThat(result).isEmpty();
        }
    }

    class BeanWithPublicFieldColumnAnnotated {
        @Column(value = {"first field", "field 1"}, description = "test description", mandatory = false, defaultValue = "test")
        public String field;
        @Column
        public String fieldWithDefaultValues;
        @Column
        @Ignore
        public String fieldWithIgnoreAndColumn;
    }

    class BeanWithPublicFieldNotAnnotated {
        public String field;
        @Ignore
        public String ignoreField;
        private String privateFieldWithSetter;
        private String privateFieldWithoutSetter;
        private String privateFieldWithMalformedSetter;

        public void setPrivateFieldWithSetter(String value) {

        }
    }

    record RecordWithPublicFieldColumnAnnotated(
        @Column(value = {"first field", "field 1"}, description = "test description", mandatory = false, defaultValue = "test")
        String field,
        @Column
        String fieldWithDefaultValues,
        @Column
        @Ignore
        String fieldWithIgnoreAndColumn
    ) {}

    record RecordWithPublicFieldNotAnnotated(
        String field,
        @Ignore
        String ignoreField
    ) {}
}
