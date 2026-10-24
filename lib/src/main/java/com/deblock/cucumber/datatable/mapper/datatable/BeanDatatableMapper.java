package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MalformedBeanException;
import com.deblock.cucumber.datatable.mapper.MapperFactory;
import com.deblock.cucumber.datatable.mapper.ObjectCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BeanDatatableMapper extends BaseObjectDatatableMapper<BeanDatatableMapper.FieldData> {
    private final Constructor<?> constructor;

    public BeanDatatableMapper(Class<?> clazz, MapperFactory mapperFactory, ColumnName parentName, FieldResolver fieldResolver) {
        super(buildFieldsData(clazz, mapperFactory, parentName, fieldResolver));
        try {
            this.constructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new MalformedBeanException("Class " + clazz.getSimpleName() + ": should have a public constructor without parameter");
        }
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final Object instance;
        final var className = this.constructor.getDeclaringClass().getSimpleName();
        try {
            instance = this.constructor.newInstance();
        } catch (InvocationTargetException e) {
            throw new ObjectCreationException("Class " + className + ": constructor threw an exception", e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ObjectCreationException("Class " + className + ": can not call the constructor", e);
        }
        this.fields.forEach(it -> it.setter.accept(instance, it.convert(entry)));
        return instance;
    }

    private static List<FieldData> buildFieldsData(Class<?> clazz, MapperFactory mapperFactory, ColumnName parentColumnName, FieldResolver fieldResolver) {
        return Arrays.stream(clazz.getDeclaredFields())
                .flatMap(field ->
                    fieldResolver.fieldInfo(field, clazz).stream()
                                .map(fieldInfo -> buildFieldData(clazz, field, mapperFactory, parentColumnName, fieldInfo))
                )
                .toList();
    }

    private static FieldData buildFieldData(Class<?> clazz, Field field, MapperFactory mapperFactory, ColumnName parentColumnName, FieldResolver.FieldInfo fieldInfo) {
        final var setter = BeanSetters.findSetter(clazz, field);
        final var datatableMapper = mapperFactory.build(
            fieldInfo,
            parentColumnName.addChild(fieldInfo.columnName()),
            setter.map(it -> it.getGenericParameterTypes()[0]).orElseGet(field::getGenericType)
        );

        if (setter.isPresent()) {
            return new FieldData(datatableMapper, (bean, object) -> {
                try {
                    setter.get().invoke(bean, object);
                } catch (InvocationTargetException e) {
                    throw new ObjectCreationException("Class " + clazz.getSimpleName() + ": setter " + setter.get().getName() + " threw an exception", e.getCause());
                } catch (IllegalAccessException e) {
                    throw new ObjectCreationException("Class " + clazz.getSimpleName() + ": can not call the setter " + setter.get().getName(), e);
                }
            });
        } else if (Modifier.isPublic(field.getModifiers())) {
            return new FieldData(datatableMapper, (bean, object) -> {
                try {
                    field.set(bean, object);
                } catch (IllegalAccessException e) {
                    throw new ObjectCreationException("Class " + clazz.getSimpleName() + ": can not set the field " + field.getName(), e);
                }
            });
        } else {
            throw new MalformedBeanException("Class " + clazz.getSimpleName() + ": the field " + field.getName() + " should be public or have a setter " + BeanSetters.setterName(field));
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
}
