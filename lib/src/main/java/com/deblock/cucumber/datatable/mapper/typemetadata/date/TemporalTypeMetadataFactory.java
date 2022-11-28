package com.deblock.cucumber.datatable.mapper.typemetadata.date;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

public class TemporalTypeMetadataFactory implements TypeMetadataFactory {
    private final Map<Class<?>, TypeMetadata> temporalTypeMetadataByTypes;

    public TemporalTypeMetadataFactory(GetDateService getDateService) {
        this.temporalTypeMetadataByTypes = Map.of(
                OffsetDateTime.class, new DateTimeTypeMetadata(getDateService::now, OffsetDateTime::parse),
                LocalDateTime.class, new DateTimeTypeMetadata(() -> getDateService.now().toLocalDateTime(), LocalDateTime::parse),
                LocalDate.class, new DateTypeMetadata(() -> getDateService.now().toLocalDate(), LocalDate::parse)
        );
    }

    @Override
    public TypeMetadata build(Type type) {
        return this.temporalTypeMetadataByTypes.get(type);
    }
}
