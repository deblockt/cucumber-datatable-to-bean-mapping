package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTypeMetadataTest {
    private final static LocalDate NOW = LocalDate.of(2010, 10, 10);

    private TypeMetadata typeMetadata;

    @BeforeEach
    public void setUp() {
        this.typeMetadata = new DateTypeMetadata(() -> NOW, LocalDate::parse);
    }

    @Test
    public void shouldReturnNowForNow() {
        final var result = typeMetadata.convert("now");

        assertThat(result).isEqualTo(NOW);
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
        final var result = typeMetadata.convert("2022-11-28");

        assertThat(result).isEqualTo(LocalDate.parse("2022-11-28"));
    }
}
