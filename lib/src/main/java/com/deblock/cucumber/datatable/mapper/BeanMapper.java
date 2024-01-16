package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Locale.ENGLISH;

public class BeanMapper implements DatatableMapper {
    private final List<DatatableHeader> headers = new ArrayList<>();
    private final List<FieldData> fieldDataByColumnName = new ArrayList<>();
    private final Class<?> clazz;

    public BeanMapper(Class<?> clazz, TypeMetadataFactory typeMetadataFactory) {
        this.clazz = clazz;
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> this.buildFieldData(field, typeMetadataFactory))
                .forEach(fieldData -> {
                    fieldDataByColumnName.add(fieldData);
                    this.headers.add(fieldData.header);
                });
    }

    @Override
    public List<DatatableHeader> headers() {
        return this.headers;
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final Object instance;
        try {
            instance = this.clazz.getConstructor().newInstance();
            this.fieldDataByColumnName.forEach(it -> it.setter.accept(instance, entry));
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new MalformedBeanException("Class " + this.clazz.getSimpleName() + ": should have a public constructor without parameter");
        }
    }

    private FieldData buildFieldData(Field field, TypeMetadataFactory typeMetadataFactory) {
        final var column = field.getAnnotation(Column.class);
        final var setterName = "set" + capitalize(field.getName());
        final var setters = Arrays.stream(this.clazz.getMethods())
                .filter(method -> method.getName().equals(setterName))
                .filter(method -> method.getParameters().length == 1)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();

        final var header = new DatatableHeader(
                column,
                field.getName(),
                typeMetadataFactory.build(setters.isEmpty() ? field.getGenericType() : setters.get().getGenericParameterTypes()[0])
        );
        if (setters.isPresent()) {
            return new FieldData(header, (bean, entry) -> {
                var cellValue = header.defaultValue();
                for (final var headerName : header.names()) {
                    if (entry.containsKey(headerName)) {
                        cellValue = entry.get(headerName);
                    }
                }
                if (cellValue != null) {
                    try {
                        setters.get().invoke(bean, header.typeMetadata().convert(cellValue));
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else if (Modifier.isPublic(field.getModifiers())) {
            return new FieldData(header, (bean, entry) -> {
                var cellValue = header.defaultValue();
                for (final var headerName : header.names()) {
                    if (entry.containsKey(headerName)) {
                        cellValue = entry.get(headerName);
                    }
                }
                if (cellValue != null) {
                    try {
                        field.set(bean, header.typeMetadata().convert(cellValue));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } else {
            throw new MalformedBeanException("Class " + this.clazz.getSimpleName() + ": the field " + field.getName() + " should be public or have a setter " + setterName);
        }
    }

    record FieldData(
            DatatableHeader header,
            BiConsumer<Object, Map<String, String>> setter
    ) {}

    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
}
