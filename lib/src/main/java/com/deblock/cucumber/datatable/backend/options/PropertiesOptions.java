package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import io.cucumber.core.exception.CucumberException;

import java.util.Map;

import static com.deblock.cucumber.datatable.backend.options.PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME;
import static com.deblock.cucumber.datatable.backend.options.PropertiesConstants.NAME_BUILDER_CLASS_PROPERTY_NAME;

public class PropertiesOptions implements FullOptions {
    private final Map<String, String> properties;

    public PropertiesOptions(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public Class<? extends ColumnNameBuilder> getNameBuilderClass() {
        return getClassParameter(NAME_BUILDER_CLASS_PROPERTY_NAME, ColumnNameBuilder.class);
    }

    @Override
    public Class<? extends FieldResolver> getFieldResolverClass() {
        return getClassParameter(FIELD_RESOLVER_CLASS_PROPERTY_NAME, FieldResolver.class);
    }

    private <T> Class<T> getClassParameter(String property, Class<T> loaderClazz) {
        final String className = properties.get(property);
        if (className == null) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            if (!loaderClazz.isAssignableFrom(clazz)) {
                throw new CucumberException("Name builder class '%s' was not a subclass of '%s'.".formatted(clazz, loaderClazz));
            }
            return (Class<T>) clazz;
        } catch (ClassNotFoundException e) {
            throw new CucumberException(
                    "The class '%s' of configuration '%s' is not found.".formatted(className, property),
                    e
            );
        }
    }
}
