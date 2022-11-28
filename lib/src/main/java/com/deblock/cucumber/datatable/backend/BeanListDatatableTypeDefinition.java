package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.BeanMapper;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

public class BeanListDatatableTypeDefinition implements DataTableTypeDefinition {

    private final Class<?> glueClass;
    private final DataTableValidator validator;
    private final BeanMapper beanMapper;

    public BeanListDatatableTypeDefinition(Class<?> glueClass, DataTableValidator validator, BeanMapper beanMapper) {
        this.glueClass = glueClass;
        this.validator = validator;
        this.beanMapper = beanMapper;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableEntryTransformer<Object>) entry -> {
                    this.validator.validate(entry.keySet());
                    return this.beanMapper.convert(entry);
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
