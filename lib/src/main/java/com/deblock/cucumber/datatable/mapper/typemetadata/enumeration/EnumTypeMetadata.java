package com.deblock.cucumber.datatable.mapper.typemetadata.enumeration;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class EnumTypeMetadata implements TypeMetadata {
    private final Class<?> clazz;
    private final String valuesString;
    private final String firstValue;

    public EnumTypeMetadata(Class<?> clazz) {
        this.clazz = clazz;

        try {
            final var method = clazz.getDeclaredMethod("values");
            final var values = method.invoke(null);
            this.valuesString = Arrays.stream(((Object[]) values)).map(Object::toString).collect(Collectors.joining(", "));
            this.firstValue = ((Object[]) values)[0].toString();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String typeDescription() {
        return this.clazz.getSimpleName() + ": " + this.valuesString;
    }

    @Override
    public String sample() {
        return this.firstValue;
    }

    @Override
    public Object convert(String value) throws ConversionError {
        try {
            final var valueOf = this.clazz.getDeclaredMethod("valueOf", String.class);
            return valueOf.invoke(null, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
