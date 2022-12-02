package com.deblock.cucumber.datatable.mapper.typemetadata.custom;


import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.beans.custom.CustomBean;
import io.cucumber.core.resource.ClasspathScanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

public class CustomTypeMetadataFactoryTest {

    @Test
    public void shouldAllowToRegisterCustomType() {
        final var factory = new CustomTypeMetadataFactory(
                new ClasspathScanner(
                        () -> this.getClass().getClassLoader()
                ),
                List.of(
                        URI.create("classpath:/com/deblock/cucumber/datatable/mapper/beans/custom")
                )
        );

        final var getTypeMetadata = factory.build(CustomBean.class);

        Assertions.assertThat(getTypeMetadata).isNotNull();
        Assertions.assertThat(getTypeMetadata).isInstanceOf(CustomJavaBeanTypeMetadata.class);
    }

    @Test
    public void shouldReturnNullIfTypeIsNotRegistered() {
        final var factory = new CustomTypeMetadataFactory(
                new ClasspathScanner(
                        () -> this.getClass().getClassLoader()
                ),
                List.of(
                        URI.create("classpath:/com/deblock/cucumber/datatable/mapper/beans/custom")
                )
        );

        final var getTypeMetadata = factory.build(Bean.class);

        Assertions.assertThat(getTypeMetadata).isNull();
    }
}
