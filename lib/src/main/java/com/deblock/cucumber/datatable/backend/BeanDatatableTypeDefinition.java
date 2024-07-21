package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.backend.exceptions.DatatableMappingException;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.mapper.datatable.exception.CellMappingException;
import com.deblock.cucumber.datatable.validator.DataTableDoesNotMatch;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.CucumberInvocationTargetException;
import io.cucumber.core.backend.DataTableTypeDefinition;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableTransformer;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
                    final Map<String, String> map = new HashMap<>();
                    final long firstRowNumberOfHeader = numberOfCommonHeader(table.row(0));
                    final long firstColumnNumberOfHeader = numberOfCommonHeader(table.column(0));
                    final boolean isTwoColumnTable = table.row(0).size() == 2;

                    if (
                            firstRowNumberOfHeader > firstColumnNumberOfHeader
                            || (firstRowNumberOfHeader == firstColumnNumberOfHeader && !isTwoColumnTable)
                    ) {
                        // table using only one row
                        for (var i = 0; i < table.width(); ++i) {
                            map.put(table.row(0).get(i), table.row(1).get(i));
                        }
                    } else {
                        // table using 2 columns mode
                        for (var i = 0; i < table.height(); ++i) {
                            map.put(table.column(0).get(i), table.column(1).get(i));
                        }
                    }
                    map.values().removeAll(Collections.singleton(null));
                   try {
                        this.validator.validate(map.keySet());
                        return this.datatableMapper.convert(map);
                   } catch (DataTableDoesNotMatch | CellMappingException exception) {
                        throw new CucumberInvocationTargetException(
                                this,
                                new InvocationTargetException(
                                    new DatatableMappingException(this.glueClass, exception)
                                )
                        );
                   }
                });
    }

    private long numberOfCommonHeader(List<String> firstRow) {
        return this.datatableMapper.headers().stream()
                .filter(header ->
                        header.names().hasOneNameEquals(new ColumnName(firstRow))
                )
                .count();
    }

    @Override
    public boolean isDefinedAt(StackTraceElement stackTraceElement) {
        return true;
    }

    @Override
    public String getLocation() {
        return null;
    }


}
