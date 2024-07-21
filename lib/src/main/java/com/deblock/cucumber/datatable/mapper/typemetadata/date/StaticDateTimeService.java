package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import java.time.OffsetDateTime;

public class StaticDateTimeService implements DateTimeService {
    private static final OffsetDateTime NOW = OffsetDateTime.now();

    @Override
    public OffsetDateTime now() {
        return NOW;
    }
}
