package com.deblock.cucumber.datatable.mapper.datatable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Locale.ENGLISH;

/**
 * Find the public setter of a bean field, following the {@code setFieldName(value)} convention.
 */
public final class BeanSetters {
    private BeanSetters() {
    }

    public static String setterName(Field field) {
        return "set" + capitalize(field.getName());
    }

    public static Optional<Method> findSetter(Class<?> clazz, Field field) {
        final var setterName = setterName(field);
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(setterName))
                .filter(method -> method.getParameterCount() == 1)
                .findFirst();
    }

    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
}
