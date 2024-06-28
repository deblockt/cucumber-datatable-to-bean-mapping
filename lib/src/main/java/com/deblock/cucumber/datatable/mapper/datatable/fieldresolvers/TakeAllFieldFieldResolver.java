package com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers;

import com.deblock.cucumber.datatable.annotations.Ignore;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.FieldResolver;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Locale.ENGLISH;

// TODO find a name
/**
 * Register all class field as datatable column.
 * If the field is annotated with @Column annotation information are taken in account.
 * If the field is non annotated with @Column annotation it's optional, without description nor default value.
 */
public class TakeAllFieldFieldResolver implements FieldResolver {
    private ColumnNameBuilder columnNameBuilder;
    private DeclarativeFieldResolver declarativeFieldResolver;

    @Override
    public void configure(ColumnNameBuilder columnNameBuilder) {
        this.columnNameBuilder = columnNameBuilder;
        this.declarativeFieldResolver = new DeclarativeFieldResolver();
        this.declarativeFieldResolver.configure(columnNameBuilder);
    }

    @Override
    public Optional<FieldInfo> fieldInfo(Field field, Class<?> clazz) {
        if (field.isAnnotationPresent(Ignore.class) || !isSettable(clazz, field)) {
            return Optional.empty();
        }
        final var declarativeFieldInfo = this.declarativeFieldResolver.fieldInfo(field, clazz);
        if (declarativeFieldInfo.isPresent()) {
            return declarativeFieldInfo;
        }
        return Optional.of(
                new FieldInfo(
                        new ColumnName(columnNameBuilder.build(field.getName())),
                        true,
                        "",
                        null
                )
        );
    }

    @Override
    public Optional<FieldInfo> fieldInfo(RecordComponent component, Class<?> clazz) {
        if (component.isAnnotationPresent(Ignore.class)) {
            return Optional.empty();
        }
        final var declarativeFieldInfo = this.declarativeFieldResolver.fieldInfo(component, clazz);
        if (declarativeFieldInfo.isPresent()) {
            return declarativeFieldInfo;
        }
        return Optional.of(
                new FieldInfo(
                        new ColumnName(columnNameBuilder.build(component.getName())),
                        true,
                        "",
                        null
                )
        );
    }

    private boolean isSettable(Class<?> clazz, Field field) {
        if (Modifier.isPublic(field.getModifiers())) {
            return true;
        }
        final var setterName = "set" + capitalize(field.getName());
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(setterName))
                .filter(method -> method.getParameters().length == 1)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst()
                .isPresent();
    }

    private static String capitalize(String name) {
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
}
