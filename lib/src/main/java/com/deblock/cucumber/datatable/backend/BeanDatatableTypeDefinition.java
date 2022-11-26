package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;

public class BeanDatatableTypeDefinition implements DataTableTypeDefinition  {
    private final Class<?> glueClass;
    private final DataTableValidator validator;

    public BeanDatatableTypeDefinition(Class<?> glueClass, DataTableValidator validator) {
        this.glueClass = glueClass;
        this.validator = validator;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableTransformer<Object>) table -> {
                    try {
                        // table using only one row
                        this.validator.validate(table.row(0));
                        return BeanDatatableTypeDefinition.this.glueClass.newInstance();
                    } catch (Exception e) {
                        // table using 2 columns mode
                        this.validator.validate(table.column(0));
                        return BeanDatatableTypeDefinition.this.glueClass.newInstance();
                    }
                });
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
