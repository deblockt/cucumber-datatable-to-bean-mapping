package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeTypeMetadataTest {
    private final static OffsetDateTime NOW = OffsetDateTime.of(2010, 10, 10, 10, 10, 10, 10, ZoneOffset.UTC);

    private TypeMetadata typeMetadata;

    @BeforeEach
    public void setUp() {
        this.typeMetadata = new DateTimeTypeMetadata(() -> NOW, OffsetDateTime::parse);
    }

    @Test
    public void shouldReturnNowForNow() {
        final var result = typeMetadata.convert("now");

        assertThat(result).isEqualTo(NOW);
    }

    @Test
    public void shouldAllowToAddXMilliAtNow() {
        final var result = typeMetadata.convert("now + 10 millis");

        assertThat(result).isEqualTo(NOW.plusNanos(10000000));
    }

    @Test
    public void shouldAllowToAddXSecondsAtNow() {
        final var result = typeMetadata.convert("now + 100 seconds");

        assertThat(result).isEqualTo(NOW.plusSeconds(100));
    }

    @Test
    public void shouldAllowToAddXMinutesAtNow() {
        final var result = typeMetadata.convert("now + 1 minute");

        assertThat(result).isEqualTo(NOW.plusMinutes(1));
    }

    @Test
    public void shouldAllowToAddXHoursAtNow() {
        final var result = typeMetadata.convert("now + 145 hours");

        assertThat(result).isEqualTo(NOW.plusHours(145));
    }

    @Test
    public void shouldAllowToAddXDayAtNow() {
        final var result = typeMetadata.convert("now + 1 day");

        assertThat(result).isEqualTo(NOW.plusDays(1));
    }

    @Test
    public void shouldAllowToAddXMonthAtNow() {
        final var result = typeMetadata.convert("now + 2 months");

        assertThat(result).isEqualTo(NOW.plusMonths(2));
    }

    @Test
    public void shouldAllowToAddXWeeksAtNow() {
        final var result = typeMetadata.convert("now + 54 weeks");

        assertThat(result).isEqualTo(NOW.plusWeeks(54));
    }

    @Test
    public void shouldAllowToAddXYearAtNow() {
        final var result = typeMetadata.convert("now + 2 years");

        assertThat(result).isEqualTo(NOW.plusYears(2));
    }

    @Test
    public void shouldAllowToSubtractXYearAtNow() {
        final var result = typeMetadata.convert("now - 134 years");

        assertThat(result).isEqualTo(NOW.minusYears(134));
    }

    @Test
    public void shouldSupportIsoFormat() {
        final var result = typeMetadata.convert("2022-11-28T08:48:45.939269+01:00");

        assertThat(result).isEqualTo(OffsetDateTime.parse("2022-11-28T08:48:45.939269+01:00"));
    }

    @Test
    public void shouldSupportLocalDateTimeUsingNow() {
        final var now = LocalDateTime.now();
        final var localDateTimeTypeMetadata = new DateTimeTypeMetadata(() -> now, LocalDateTime::parse);
        final var result = localDateTimeTypeMetadata.convert("now + 10 day");

        assertThat(result).isEqualTo(now.plusDays(10));
    }

    @Test
    public void shouldSupportLocalDateTimeUsingFullDate() {
        final var now = LocalDateTime.now();
        final var localDateTimeTypeMetadata = new DateTimeTypeMetadata(() -> now, LocalDateTime::parse);
        final var result = localDateTimeTypeMetadata.convert("2022-11-28T08:52:10.816163");

        assertThat(result).isEqualTo(LocalDateTime.parse("2022-11-28T08:52:10.816163"));
    }
}
