package com.deblock.cucumber.datatable.mapper.typemetadata.enumeration;

import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;


public class EnumTypeMetadata implements TypeMetadata {
    private final Class<?> clazz;
    private final String valuesString;
    private final String firstValue;
    private final Method valueOf;

    public EnumTypeMetadata(Class<?> clazz) {
        this.clazz = clazz;

        try {
            final var method = clazz.getDeclaredMethod("values");
            final var values = validateValues((Object[])method.invoke(null));
            this.valuesString = Arrays.stream(((Object[]) values)).map(Object::toString).collect(Collectors.joining(", "));
            this.firstValue = ((Object[]) values)[0].toString();
            this.valueOf = this.clazz.getDeclaredMethod("valueOf", String.class);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] validateValues(Object[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("enum " + this.clazz.getSimpleName() + " should have at least one value");
        }
        return values;
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
            return valueOf.invoke(null, value);
        } catch (InvocationTargetException targetException) {
            if (targetException.getTargetException() instanceof IllegalArgumentException ex) {
                throw new ConversionError(ex.getMessage(), ex);
            }
            throw new ConversionError("%s can not be converted to enum %s".formatted(value, this.clazz.getName()), targetException);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
