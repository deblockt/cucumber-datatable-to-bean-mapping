package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.BeanMapper;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;

import java.util.HashMap;

public class BeanDatatableTypeDefinition implements DataTableTypeDefinition  {
    private final Class<?> glueClass;
    private final DataTableValidator validator;
    private final BeanMapper beanMapper;

    public BeanDatatableTypeDefinition(Class<?> glueClass, DataTableValidator validator, BeanMapper beanMapper) {
        this.glueClass = glueClass;
        this.validator = validator;
        this.beanMapper = beanMapper;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableTransformer<Object>) table -> {
                    try {
                        // table using only one row
                        this.validator.validate(table.row(0));
                        final var map = new HashMap<String, String>();
                        for (var i = 0; i < table.width(); ++i) {
                            map.put(table.row(0).get(i), table.row(1).get(i));
                        }
                        return this.beanMapper.convert(map);
                    } catch (Exception e) {
                        // table using 2 columns mode
                        this.validator.validate(table.column(0));

                        final var map = new HashMap<String, String>();
                        for (var i = 0; i < table.height(); ++i) {
                            map.put(table.column(0).get(i), table.column(1).get(i));
                        }
                        return this.beanMapper.convert(map);
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
