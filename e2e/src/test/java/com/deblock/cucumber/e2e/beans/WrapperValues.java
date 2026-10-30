package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@DataTableWithHeader
public record WrapperValues(
    @Column(value = "integer", mandatory = false) Integer anInteger,
    @Column(value = "long", mandatory = false) Long aLong,
    @Column(value = "short", mandatory = false) Short aShort,
    @Column(value = "float", mandatory = false) Float aFloat,
    @Column(value = "double", mandatory = false) Double aDouble,
    @Column(value = "boolean", mandatory = false) Boolean aBoolean,
    @Column(value = "big decimal", mandatory = false) BigDecimal aBigDecimal,
    @Column(value = "big integer", mandatory = false) BigInteger aBigInteger,
    @Column(value = "string", mandatory = false) String aString,
    @Column(value = "uuid", mandatory = false) UUID anUuid,
    @Column(value = "level", mandatory = false) Level level
) {
}
