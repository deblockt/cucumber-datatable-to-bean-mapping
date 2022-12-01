package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

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
                    this.validator.validate(entry.keySet());
                    return this.datatableMapper.convert(entry);
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
