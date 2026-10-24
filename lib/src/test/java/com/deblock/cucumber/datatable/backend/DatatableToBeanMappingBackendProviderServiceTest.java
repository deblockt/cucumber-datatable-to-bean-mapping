package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.backend.options.PropertiesConstants;
import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.ImplicitFieldResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

class DatatableToBeanMappingBackendProviderServiceTest {

    @AfterEach
    void clearProperties() {
        System.clearProperty(PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME);
    }

    @Test
    void shouldLoadConfiguredClassWithCucumberClassLoader() {
        System.setProperty(PropertiesConstants.FIELD_RESOLVER_CLASS_PROPERTY_NAME, ImplicitFieldResolver.class.getName());
        final var cucumberClassLoader = new ChildFirstClassLoader(ImplicitFieldResolver.class.getName(), getClass().getClassLoader());

        final var backend = new DatatableToBeanMappingBackendProviderService().create(null, null, () -> cucumberClassLoader);

        assertThatCode(() -> backend.loadGlue(null, List.of())).doesNotThrowAnyException();
    }
}
