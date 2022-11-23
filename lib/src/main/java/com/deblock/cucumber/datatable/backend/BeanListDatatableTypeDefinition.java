package com.deblock.cucumber.datatable.backend;

import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

public class BeanListDatatableTypeDefinition implements DataTableTypeDefinition {

    private final Class<?> glueClass;

    public BeanListDatatableTypeDefinition(Class<?> glueClass) {
        this.glueClass = glueClass;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableEntryTransformer<Object>) entry -> BeanListDatatableTypeDefinition.this.glueClass.newInstance()
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
