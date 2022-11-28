package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class TemporalTypeMetadataFactoryTest {

    @Test
    public void canReturnOffsetDateTime() {
        final var factory = new TemporalTypeMetadataFactory(OffsetDateTime::now);

        final var result = factory.build(OffsetDateTime.class).convert("now");

        Assertions.assertThat(result).isInstanceOf(OffsetDateTime.class);
    }

    @Test
    public void canReturnLocalDateTime() {
        final var factory = new TemporalTypeMetadataFactory(OffsetDateTime::now);

        final var result = factory.build(LocalDateTime.class).convert("now");

        Assertions.assertThat(result).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void canReturnLocalDate() {
        final var factory = new TemporalTypeMetadataFactory(OffsetDateTime::now);

        final var result = factory.build(LocalDate.class).convert("now");

        Assertions.assertThat(result).isInstanceOf(LocalDate.class);
    }
}
