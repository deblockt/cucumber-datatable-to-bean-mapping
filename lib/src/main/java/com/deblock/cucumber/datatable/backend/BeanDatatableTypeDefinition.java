package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BeanDatatableTypeDefinition implements DataTableTypeDefinition  {
    private final Class<?> glueClass;
    private final DataTableValidator validator;
    private final DatatableMapper datatableMapper;

    public BeanDatatableTypeDefinition(Class<?> glueClass, DataTableValidator validator, DatatableMapper datatableMapper) {
        this.glueClass = glueClass;
        this.validator = validator;
        this.datatableMapper = datatableMapper;
    }

    @Override
    public DataTableType dataTableType() {
        return new DataTableType(
                this.glueClass,
                (TableTransformer<Object>) table -> {
                    Map<String, String> map;
                    try {
                        map = new HashMap<>();
                        // table using only one row
                        for (var i = 0; i < table.width(); ++i) {
                            map.put(table.row(0).get(i), table.row(1).get(i));
                        }
                        map.values().removeAll(Collections.singleton(null));
                        this.validator.validate(map.keySet());
                    } catch (Exception e) {
                        map = new HashMap<>();
                        // table using 2 columns mode
                        for (var i = 0; i < table.height(); ++i) {
                            map.put(table.column(0).get(i), table.column(1).get(i));
                        }
                        map.values().removeAll(Collections.singleton(null));
                        this.validator.validate(map.keySet());
                    }
                    return this.datatableMapper.convert(map);
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
