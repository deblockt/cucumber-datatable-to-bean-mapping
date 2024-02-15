package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import com.deblock.cucumber.datatable.data.TypeMetadata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CustomJavaBeanTypeMetadata implements TypeMetadata {
    private final Method mapper;
    private final CustomDatatableFieldMapper annotation;

    public CustomJavaBeanTypeMetadata(Method mapper, CustomDatatableFieldMapper annotation) {
        this.mapper = validateMapper(mapper);
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
            return this.mapper.invoke(null, value);
        } catch (IllegalAccessException e) {
            throw new ConversionError("Unable to convert \"" + value + "\" using " + this.getMethodName(this.mapper) + " to return " + this.mapper.getReturnType().getSimpleName(), e);
        } catch (InvocationTargetException e) {
            throw new ConversionError(e.getTargetException().getMessage(), e);
        }
    }

    private Method validateMapper(Method mapper) {
        if (!Modifier.isStatic(mapper.getModifiers())) {
            throw new IllegalArgumentException(
                    "The method " + this.getMethodName(mapper) +
                            " should be static to be used with annotation CustomDatatableFieldMapper"
            );
        }
        if (mapper.getParameterCount() != 1 || !String.class.equals(mapper.getParameters()[0].getType())) {
            throw new IllegalArgumentException(
                    "The method " + this.getMethodName(mapper) +
                            " should have one String parameter to be used with annotation CustomDatatableFieldMapper"
            );
        }

        return mapper;
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
}
