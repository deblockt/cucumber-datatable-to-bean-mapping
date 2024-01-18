package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MalformedBeanException;
import com.deblock.cucumber.datatable.mapper.MapperFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.Locale.ENGLISH;

public class BeanDatatableMapper implements DatatableMapper {
    private final List<FieldData> fieldsData = new ArrayList<>();
    private final Class<?> clazz;

    public BeanDatatableMapper(Class<?> clazz, MapperFactory mapperFactory) {
        this.clazz = clazz;
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> this.buildFieldData(field, mapperFactory))
                .forEach(fieldsData::add);
    }

    @Override
    public List<DatatableHeader> headers() {
        return this.fieldsData.stream()
                .flatMap(it -> it.mapper.headers().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final Object instance;
        try {
            instance = this.clazz.getConstructor().newInstance();
            this.fieldsData.forEach(it -> it.setter.accept(instance, it.mapper.convert(entry)));
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new MalformedBeanException("Class " + this.clazz.getSimpleName() + ": should have a public constructor without parameter");
        }
    }

    private FieldData buildFieldData(Field field, MapperFactory mapperFactory) {
        final var column = field.getAnnotation(Column.class);
        final var setterName = "set" + capitalize(field.getName());
        final var setters = Arrays.stream(this.clazz.getMethods())
                .filter(method -> method.getName().equals(setterName))
                .filter(method -> method.getParameters().length == 1)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();

        final var header = mapperFactory.build(
            column,
            field.getName(),
            setters.isEmpty() ? field.getGenericType() : setters.get().getGenericParameterTypes()[0]
        );

        if (setters.isPresent()) {
            return new FieldData(header, (bean, object) -> {
                try {
                    setters.get().invoke(bean, object);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (Modifier.isPublic(field.getModifiers())) {
            return new FieldData(header, (bean, object) -> {
                try {
                    field.set(bean, object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new MalformedBeanException("Class " + this.clazz.getSimpleName() + ": the field " + field.getName() + " should be public or have a setter " + setterName);
        }
    }

    record FieldData(
            DatatableMapper mapper,
            BiConsumer<Object, Object> setter
    ) {}

    private static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
}
