package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

import java.util.List;

public class BeanListDatatableTypeDefinition implements DataTableTypeDefinition {

    private final Class<?> glueClass;
    private final DataTableValidator validator;

    public BeanListDatatableTypeDefinition(Class<?> glueClass, DataTableValidator validator) {
        this.glueClass = glueClass;
        this.validator = validator;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableEntryTransformer<Object>) entry -> {
                    this.validator.validate(entry.keySet());
                    return BeanListDatatableTypeDefinition.this.glueClass.newInstance();
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
