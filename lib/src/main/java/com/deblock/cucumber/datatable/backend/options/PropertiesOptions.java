package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import io.cucumber.core.exception.CucumberException;

import java.util.Map;

import static com.deblock.cucumber.datatable.backend.options.PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME;

public class PropertiesOptions implements FullOptions {
    private final Map<String, String> properties;

    public PropertiesOptions(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        final String className = properties.get(NAME_BUILDER_CLASS_PROPERTY_NAME);
        if (className == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            if (!ColumnNameBuilder.class.isAssignableFrom(clazz)) {
                throw new CucumberException("Name builder class '%s' was not a subclass of '%s'.".formatted(clazz, ColumnNameBuilder.class));
            }
            return (Class<? extends ColumnNameBuilder>) clazz;
        } catch (ClassNotFoundException e) {
            throw new CucumberException(
                "The class '%s' of configuration '%s' is not found.".formatted(className, NAME_BUILDER_CLASS_PROPERTY_NAME),
                e
            );
        }
    }
}
