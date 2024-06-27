package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.backend.options.DefaultOptions;
import com.deblock.cucumber.datatable.backend.options.MergedOptions;
import com.deblock.cucumber.datatable.backend.options.PropertiesOptions;
import io.cucumber.core.backend.Backend;
import io.cucumber.core.backend.BackendProviderService;
import io.cucumber.core.backend.Container;
import io.cucumber.core.backend.Lookup;
import io.cucumber.core.options.CucumberProperties;

import java.util.function.Supplier;

public class DatatableToBeanMappingBackendProviderService implements BackendProviderService {
    @Override
    public Backend create(Lookup lookup, Container container, Supplier<ClassLoader> classLoader) {
        final var options = new MergedOptions(
                new PropertiesOptions(CucumberProperties.fromSystemProperties()),
                new PropertiesOptions(CucumberProperties.fromEnvironment()),
                new PropertiesOptions(CucumberProperties.fromPropertiesFile()),
                new DefaultOptions()
        );
        return new DatatableToBeanMappingBackend(classLoader, options);
    }
}
