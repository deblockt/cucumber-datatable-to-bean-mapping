package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import java.time.OffsetDateTime;

public class StaticGetTimeService implements GetDateService {
    private final OffsetDateTime NOW = OffsetDateTime.now();

    @Override
    public OffsetDateTime now() {
        return NOW;
    }
}
