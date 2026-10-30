package com.deblock.cucumber.e2e;

import com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService;

import java.time.OffsetDateTime;

/**
 * Fixes `now`, so the relative dates can be checked. Registered with SPI and cucumber.properties.
 */
public class FixedDateTimeService implements DateTimeService {
    public static final OffsetDateTime NOW = OffsetDateTime.parse("2024-01-31T10:00:00+02:00");

    @Override
    public OffsetDateTime now() {
        return NOW;
    }
}
