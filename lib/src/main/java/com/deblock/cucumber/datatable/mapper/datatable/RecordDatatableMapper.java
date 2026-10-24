package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MapperFactory;
import com.deblock.cucumber.datatable.mapper.ObjectCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecordDatatableMapper extends BaseObjectDatatableMapper<DatatableMapper> {
    private final Constructor<?> constructor;

    public RecordDatatableMapper(Class<?> recordClass, MapperFactory mapperFactory, FieldResolver fieldResolver) {
        this(recordClass, mapperFactory, new ColumnName(), fieldResolver);
    }

    public RecordDatatableMapper(Class<?> recordClass, MapperFactory mapperFactory, ColumnName columnName, FieldResolver fieldResolver) {
        super(readRecordFields(recordClass, mapperFactory, columnName, fieldResolver));

        final var types = Arrays.stream(recordClass.getRecordComponents())
                .map(RecordComponent::getType)
                .toArray(Class<?>[]::new);
        try {
            this.constructor = recordClass.getDeclaredConstructor(types);
        } catch (NoSuchMethodException e) {
            // a record always has a canonical constructor
            throw new IllegalStateException("Class " + recordClass.getSimpleName() + ": canonical constructor not found", e);
        }
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final var values = this.fields.stream()
                .map(it -> it.convert(entry))
                .toArray();
        final var className = this.constructor.getDeclaringClass().getSimpleName();
        try {
            return constructor.newInstance(values);
        } catch (InvocationTargetException e) {
            throw new ObjectCreationException("Class " + className + ": constructor threw an exception", e.getCause());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ObjectCreationException("Class " + className + ": can not call the constructor", e);
        }
    }

    private static List<DatatableMapper> readRecordFields(Class<?> clazz, MapperFactory mapperFactory, ColumnName parentName, FieldResolver fieldResolver) {
        final var components = clazz.getRecordComponents();
        return Arrays.stream(components)
                .map(recordComponent ->
                    fieldResolver.fieldInfo(recordComponent, clazz)
                            .map(fieldInfo -> buildDatatableMapper(recordComponent, mapperFactory, parentName, fieldInfo))
                            .orElseGet(NotMappedDatatableMapper::new)
                )
                .toList();
    }

    private static DatatableMapper buildDatatableMapper(RecordComponent recordComponent, MapperFactory mapperFactory, ColumnName parentName, FieldResolver.FieldInfo fieldInfo) {
        return mapperFactory.build(
                fieldInfo,
                parentName.addChild(fieldInfo.columnName()),
                recordComponent.getGenericType()
        );
    }
}
