package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;
import io.cucumber.core.exception.CucumberException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PropertiesOptionsTest {

    @Test
    public void getNameBuilderClassShouldThrowExceptionIfClassDoesNotExists() {
        PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME, "com.test.TestUnknown"
        ));

        final var exception = assertThrows(CucumberException.class, () -> propertiesOptions.getNameBuilderClass());

        assertThat(exception).hasMessage("The class 'com.test.TestUnknown' of configuration 'cucumber.datatable.mapper.name-builder-class' is not found.");
    }

    @Test
    public void getNameBuilderClassShouldThrowExceptionIfClassDoesNotExtendsNameBuilder() {
        PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest"
        ));

        final var exception = assertThrows(CucumberException.class, () -> propertiesOptions.getNameBuilderClass());

        assertThat(exception).hasMessage("Name builder class 'class com.deblock.cucumber.datatable.backend.options.PropertiesOptionsTest' was not a subclass of 'interface com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder'.");
    }

    @Test
    public void getNameBuilderClassShouldReturnTheClassIfItIsOk() {
        PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of(
                PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME, "com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder"
        ));

        final var result = propertiesOptions.getNameBuilderClass();

        assertThat(result).isEqualTo(HumanReadableColumnNameBuilder.class);
    }

    @Test
    public void getNameBuilderReturnNullIfPropertyIsNotSet() {
        PropertiesOptions propertiesOptions = new PropertiesOptions(Map.of());

        final var result = propertiesOptions.getNameBuilderClass();

        assertThat(result).isNull();
    }
}
