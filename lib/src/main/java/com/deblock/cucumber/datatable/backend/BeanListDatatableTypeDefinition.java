package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

import java.util.Collections;
import java.util.HashMap;

public class BeanListDatatableTypeDefinition implements DataTableTypeDefinition {

    private final Class<?> glueClass;
    private final DataTableValidator validator;
    private final DatatableMapper datatableMapper;

    public BeanListDatatableTypeDefinition(Class<?> glueClass, DataTableValidator validator, DatatableMapper datatableMapper) {
        this.glueClass = glueClass;
        this.validator = validator;
        this.datatableMapper = datatableMapper;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableEntryTransformer<Object>) entry -> {
                    final var map =  new HashMap<>(entry);
                    map.values().removeAll(Collections.singleton(null));
                    this.validator.validate(map.keySet());
                    return this.datatableMapper.convert(map);
                }
        );
    }

    @Override
    public boolean isDefinedAt(StackTraceElement stackTraceElement) {
        return false;
    }

    @Override
    public String getLocation() {
        return "unknown";
    }
}
