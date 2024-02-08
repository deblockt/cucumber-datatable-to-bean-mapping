package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MalformedBeanException;
import com.deblock.cucumber.datatable.mapper.MapperFactory;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilderChain;
import com.deblock.cucumber.datatable.mapper.name.FromAnnotationColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.FromFieldNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.WithParentNameBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.Locale.ENGLISH;

public class BeanDatatableMapper extends BaseObjectDatatableMapper<BeanDatatableMapper.FieldData> {
    private final Constructor<?> constructor;

    public BeanDatatableMapper(Class<?> clazz, MapperFactory mapperFactory) {
        this(clazz, mapperFactory, null);
    }

    public BeanDatatableMapper(Class<?> clazz, MapperFactory mapperFactory, ColumnNameBuilder parentNameBuilder) {
        super(buildFieldsData(clazz, mapperFactory, parentNameBuilder));
        try {
            this.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new MalformedBeanException("Class " + clazz.getSimpleName() + ": should have a public constructor without parameter");
        }
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final Object instance;
        try {
            instance = this.constructor.newInstance();
            this.fields.forEach(it -> it.setter.accept(instance, it.convert(entry)));
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<FieldData> buildFieldsData(Class<?> clazz, MapperFactory mapperFactory, ColumnNameBuilder columnNameBuilder) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(field -> buildFieldData(clazz, field, mapperFactory, columnNameBuilder))
                .toList();
    }

    private static FieldData buildFieldData(Class<?> clazz, Field field, MapperFactory mapperFactory, ColumnNameBuilder columnNameBuilder) {
        final var column = field.getAnnotation(Column.class);
        final var setterName = "set" + capitalize(field.getName());
        final var setter = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(setterName))
                .filter(method -> method.getParameters().length == 1)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .findFirst();

        final var datatableMapper = mapperFactory.build(
            column,
            new WithParentNameBuilder(
                    columnNameBuilder,
                    new ColumnNameBuilderChain(new FromAnnotationColumnNameBuilder(column), new FromFieldNameBuilder(field))
            ),
            setter.map(it -> it.getGenericParameterTypes()[0]).orElseGet(field::getGenericType)
        );

        if (setter.isPresent()) {
            return new FieldData(datatableMapper, (bean, object) -> {
                try {
                    setter.get().invoke(bean, object);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (Modifier.isPublic(field.getModifiers())) {
            return new FieldData(datatableMapper, (bean, object) -> {
                try {
                    field.set(bean, object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new MalformedBeanException("Class " + clazz.getSimpleName() + ": the field " + field.getName() + " should be public or have a setter " + setterName);
        }
    }

    protected static class FieldData implements DatatableMapper {
        private DatatableMapper mapper;
        private BiConsumer<Object, Object> setter;

        public FieldData(DatatableMapper mapper, BiConsumer<Object, Object> setter) {
            this.mapper = mapper;
            this.setter = setter;
        }

        @Override
        public List<DatatableHeader> headers() {
            return mapper.headers();
        }

        @Override
        public Object convert(Map<String, String> entry) {
            return mapper.convert(entry);
        }
    }

    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }
}
