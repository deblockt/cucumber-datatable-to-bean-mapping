package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.backend.ChildFirstClassLoader;
import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver;
import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService;
import io.cucumber.core.exception.CucumberException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesOptionsTest {
    private static final Supplier<ClassLoader> CLASS_LOADER = PropertiesOptionsTest.class::getClassLoader;

    @Nested
    class GetNameBuilderClassMethod {
        @Test
        void shouldThrowExceptionIfClassDoesNotExists() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME, "com.test.TestUnknown"
            ), CLASS_LOADER);

            final var exception = assertThrows(CucumberException.class, propertiesOptions::getNameBuilderClass);

            assertThat(exception).hasMessage("The class 'com.test.TestUnknown' of configuration 'cucumber.datatable.mapper.name-builder-class' is not found.");
        }

        @Test
        void shouldThrowExceptionIfClassDoesNotExtendsNameBuilder() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest"
            ), CLASS_LOADER);

            final var exception = assertThrows(CucumberException.class, propertiesOptions::getNameBuilderClass);

            assertThat(exception).hasMessage("The class 'class com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest' was not a subclass of 'interface com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder'.");
        }

        @Test
        void shouldReturnTheClassIfItIsOk() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder"
            ), CLASS_LOADER);

            final var result = propertiesOptions.getNameBuilderClass();

            assertThat(result).isEqualTo(HumanReadableColumnNameBuilder.class);
        }

        @Test
        void returnNullIfPropertyIsNotSet() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(), CLASS_LOADER);

            final var result = propertiesOptions.getNameBuilderClass();

            assertThat(result).isNull();
        }
    }

    @Nested
    class GetFieldResolverClassMethod {
        @Test
        void shouldThrowExceptionIfClassDoesNotExists() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME, "com.test.TestUnknown"
            ), CLASS_LOADER);

            final var exception = assertThrows(CucumberException.class, propertiesOptions::getFieldResolverClass);

            assertThat(exception).hasMessage("The class 'com.test.TestUnknown' of configuration 'cucumber.datatable.mapper.field-resolver-class' is not found.");
        }

        @Test
        void shouldThrowExceptionIfClassDoesNotExtendsNameBuilder() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest"
            ), CLASS_LOADER);

            final var exception = assertThrows(CucumberException.class, propertiesOptions::getFieldResolverClass);

            assertThat(exception).hasMessage("The class 'class com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest' was not a subclass of 'interface com.deblock.cucumber.datatable.mapper.datatable.FieldResolver'.");
        }

        @Test
        void shouldReturnTheClassIfItIsOk() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver"
            ), CLASS_LOADER);

            final var result = propertiesOptions.getFieldResolverClass();

            assertThat(result).isEqualTo(DeclarativeFieldResolver.class);
        }

        @Test
        void shouldLoadTheClassWithTheGivenClassLoader() {
            final var classLoader = new ChildFirstClassLoader(DeclarativeFieldResolver.class.getName(), CLASS_LOADER.get());
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME, DeclarativeFieldResolver.class.getName()
            ), () -> classLoader);

            final var result = propertiesOptions.getFieldResolverClass();

            assertThat(result.getName()).isEqualTo(DeclarativeFieldResolver.class.getName());
            assertThat(result.getClassLoader()).isSameAs(classLoader);
        }

        @Test
        void returnNullIfPropertyIsNotSet() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(), CLASS_LOADER);

            final var result = propertiesOptions.getFieldResolverClass();

            assertThat(result).isNull();
        }
    }

    @Nested
    class GetDateTimeServiceClassMethod {
        @Test
        void shouldThrowExceptionIfClassDoesNotExists() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.DATE_TIME_SERVICE_CLASS_PROPERTY_NAME, "com.test.TestUnknown"
            ), CLASS_LOADER);

            final var exception = assertThrows(CucumberException.class, propertiesOptions::getDateTimeServiceClass);

            assertThat(exception).hasMessage("The class 'com.test.TestUnknown' of configuration 'cucumber.datatable.mapper.date-time-service-class' is not found.");
        }

        @Test
        void shouldThrowExceptionIfClassDoesNotExtendsNameBuilder() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.DATE_TIME_SERVICE_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest"
            ), CLASS_LOADER);

            final var exception = assertThrows(CucumberException.class, propertiesOptions::getDateTimeServiceClass);

            assertThat(exception).hasMessage("The class 'class com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest' was not a subclass of 'interface com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService'.");
        }

        @Test
        void shouldReturnTheClassIfItIsOk() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                    PropertiesConstants.DATE_TIME_SERVICE_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService"
            ), CLASS_LOADER);

            final var result = propertiesOptions.getDateTimeServiceClass();

            assertThat(result).isEqualTo(StaticDateTimeService.class);
        }

        @Test
        void returnNullIfPropertyIsNotSet() {
            PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(), CLASS_LOADER);

            final var result = propertiesOptions.getDateTimeServiceClass();

            assertThat(result).isNull();
        }
    }
}
