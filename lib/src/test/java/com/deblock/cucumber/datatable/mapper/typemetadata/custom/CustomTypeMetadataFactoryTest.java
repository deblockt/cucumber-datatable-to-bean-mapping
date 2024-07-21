package com.deblock.cucumber.datatable.mapper.typemetadata.custom;


import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.beans.custom.CustomBean;
import io.cucumber.core.backend.Lookup;
import io.cucumber.core.resource.ClasspathScanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;

class CustomTypeMetadataFactoryTest {
    Lookup lookup = Mockito.mock(Lookup.class);

    @Test
    void shouldAllowToRegisterCustomType() {
        final var factory = new CustomTypeMetadataFactory(
                new ClasspathScanner(
                        () -> this.getClass().getClassLoader()
                ),
                List.of(
                        URI.create("classpath:/com/deblock/cucumber/datatable/mapper/beans/custom")
                ),
                lookup
        );

        final var getTypeMetadata = factory.build(CustomBean.class);

        Assertions.assertThat(getTypeMetadata)
                .isNotNull()
                .isInstanceOf(CustomJavaBeanTypeMetadata.class);
    }

    @Test
    void shouldReturnNullIfTypeIsNotRegistered() {
        final var factory = new CustomTypeMetadataFactory(
                new ClasspathScanner(
                        () -> this.getClass().getClassLoader()
                ),
                List.of(
                        URI.create("classpath:/com/deblock/cucumber/datatable/mapper/beans/custom")
                ),
                lookup
        );

        final var getTypeMetadata = factory.build(Bean.class);

        Assertions.assertThat(getTypeMetadata).isNull();
    }
}
