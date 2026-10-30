package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@DataTableWithHeader
public record DateValues(
    @Column(value = "local date", mandatory = false) LocalDate localDate,
    @Column(value = "local date time", mandatory = false) LocalDateTime localDateTime,
    @Column(value = "offset date time", mandatory = false) OffsetDateTime offsetDateTime
) {
}
