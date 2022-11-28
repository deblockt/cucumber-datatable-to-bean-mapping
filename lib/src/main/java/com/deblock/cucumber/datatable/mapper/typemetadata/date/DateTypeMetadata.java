package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class DateTypeMetadata implements TypeMetadata {
    private final static Pattern REGEX = Pattern.compile("\\s*now(?:\\s*(?<operator>[+-])\\s*(?<amount>\\d+)\\s*(?<unit>days?|weeks?|years?|months?))?\\s*");

    private final Supplier<Temporal> nowSupplier;
    private final Function<String, Temporal> parser;

    public DateTypeMetadata(Supplier<Temporal> nowSupplier, Function<String, Temporal> parser) {
        this.nowSupplier = nowSupplier;
        this.parser = parser;
    }

    @Override
    public String typeDescription() {
        return "date";
    }

    @Override
    public String sample() {
        return "now + 1";
    }

    @Override
    public Object convert(String value) throws ConversionError {
        try {
            return this.parser.apply(value);
        } catch (Exception e) {
            final var matcher = REGEX.matcher(value);
            if (matcher.matches()) {
                final var operator = matcher.group("operator");
                final var amount = matcher.group("amount");
                final var unit = toChronosUnit(matcher.group("unit"));
                if (operator == null) {
                    return nowSupplier.get();
                } else {
                    if (operator.equals("+")) {
                        return nowSupplier.get().plus(Integer.parseInt(amount), unit);
                    } else {
                        return nowSupplier.get().minus(Integer.parseInt(amount), unit);
                    }
                }
            }

            throw new IllegalArgumentException("the value \"" + value + "\" doesn't match date format. Use ISO format or \"now + x day\"");
        }
    }

    private static ChronoUnit toChronosUnit(String unit) {
        if (unit == null) {
            return null;
        } else if (unit.endsWith("s")) {
            return ChronoUnit.valueOf(unit.toUpperCase());
        } else {
            return ChronoUnit.valueOf((unit + "s").toUpperCase());
        }
    }
}
