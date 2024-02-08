package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MapperFactory;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.ColumnNameBuilderChain;
import com.deblock.cucumber.datatable.mapper.name.FromAnnotationColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.FromComponentNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.WithParentNameBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecordDatatableMapper extends BaseObjectDatatableMapper<DatatableMapper> {
    private final Constructor<?> constructor;

    public RecordDatatableMapper(Class<?> recordClass, MapperFactory mapperFactory) {
        this(recordClass, mapperFactory, null);
    }

    public RecordDatatableMapper(Class<?> recordClass, MapperFactory mapperFactory, ColumnNameBuilder parentNameBuilder) {
        super(readRecordFields(recordClass, mapperFactory, parentNameBuilder));

        final var types = Arrays.stream(recordClass.getRecordComponents())
                .map(RecordComponent::getType)
                .toArray(Class<?>[]::new);
        try {
            this.constructor = recordClass.getDeclaredConstructor(types);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convert(Map<String, String> entry) {
        final var values = this.fields.stream()
                .map(it -> it.convert(entry))
                .toArray();
        try {
            return constructor.newInstance(values);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<DatatableMapper> readRecordFields(Class<?> clazz, MapperFactory mapperFactory, ColumnNameBuilder parentNameBuilder) {
        final var components = clazz.getRecordComponents();
        return Arrays.stream(components)
                .map(recordComponent -> {
                    if (recordComponent.isAnnotationPresent(Column.class)) {
                        return buildDatatableMapper(recordComponent, mapperFactory, parentNameBuilder);
                    } else {
                        return new NotMappedDatatableMapper();
                    }
                })
                .toList();
    }

    private static DatatableMapper buildDatatableMapper(RecordComponent recordComponent, MapperFactory mapperFactory, ColumnNameBuilder parentNameBuilder) {
        final var column = recordComponent.getAnnotation(Column.class);

        return mapperFactory.build(
                column,
                new WithParentNameBuilder(
                        parentNameBuilder,
                        new ColumnNameBuilderChain(new FromAnnotationColumnNameBuilder(column), new FromComponentNameBuilder(recordComponent))
                ),
                recordComponent.getGenericType()
        );
    }
}
