package com.deblock.cucumber.datatable.backend;

import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;

public class BeanDatatableTypeDefinition implements DataTableTypeDefinition  {
    private final Class<?> glueClass;

    public BeanDatatableTypeDefinition(Class<?> glueClass) {
        this.glueClass = glueClass;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableTransformer<Object>) table -> BeanDatatableTypeDefinition.this.glueClass.newInstance());
    }

    @Override
    public boolean isDefinedAt(StackTraceElement stackTraceElement) {
        return false;
    }

    @Override
    public String getLocation() {
        return null;
    }
}
