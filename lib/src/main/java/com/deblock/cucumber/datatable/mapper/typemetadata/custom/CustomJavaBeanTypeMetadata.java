package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import io.cucumber.core.backend.Lookup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CustomJavaBeanTypeMetadata implements TypeMetadata {
    private final Mapper mapper;
    private final CustomDatatableFieldMapper annotation;
    private final Lookup lookup;
    private final Method method;

    public CustomJavaBeanTypeMetadata(Method mapperMethod, CustomDatatableFieldMapper annotation, Lookup lookup) {
        this.lookup = lookup;
        this.mapper = validateMapper(mapperMethod);
        this.method = mapperMethod;
        this.annotation = validateAnnotation(annotation);
    }

    @Override
    public String typeDescription() {
        return this.annotation.typeDescription();
    }

    @Override
    public String sample() {
        return this.annotation.sample();
    }

    @Override
    public Object convert(String value) throws ConversionError {
        try {
            return this.mapper.map(value);
        } catch (IllegalAccessException e) {
            throw new ConversionError("Unable to convert \"" + value + "\" using " + this.getMethodName(this.method) + " to return " + this.method.getReturnType().getSimpleName(), e);
        } catch (InvocationTargetException e) {
            throw new ConversionError(e.getTargetException().getMessage(), e.getTargetException());
        }
    }

    private Mapper validateMapper(Method method) {
        if (method.getParameterCount() != 1 || !String.class.equals(method.getParameters()[0].getType())) {
            throw new IllegalArgumentException(
                    "The method " + this.getMethodName(method) +
                            " should have one String parameter to be used with annotation CustomDatatableFieldMapper"
            );
        }

        if (Modifier.isStatic(method.getModifiers())) {
            return value -> method.invoke(null, value);
        } else {
            return value -> method.invoke(lookup.getInstance(method.getDeclaringClass()), value);
        }
    }

    private String getMethodName(Method mapper) {
        return mapper.getDeclaringClass().getSimpleName() + "." + mapper.getName();
    }

    private CustomDatatableFieldMapper validateAnnotation(CustomDatatableFieldMapper annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("The annotation should not be null");
        }
        return annotation;
    }

    interface Mapper {
        Object map(String value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
    }
}
