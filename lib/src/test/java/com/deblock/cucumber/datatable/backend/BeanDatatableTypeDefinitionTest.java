package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanDatatableTypeDefinitionTest {

    @Test
    public void shouldValidateHeadersWithTableWithOneRow() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition  = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "header2"),
                        row("value", "value2")
                )
        );

        Mockito.verify(validator).validate(Set.of("header", "header2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
    }

    @Test
    public void shouldValidateHeadersWithTwoColumn() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition  = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);
        Mockito.doThrow(new RuntimeException("error")).when(validator).validate(Set.of("header", "value"));

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "value"),
                        row("header2", "value2")
                )
        );

        Mockito.verify(validator).validate(Set.of("header", "header2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
    }


    @Test
    public void shouldRemoveNullColumns() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition  = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);
        Mockito.doThrow(new RuntimeException("error")).when(validator).validate(Set.of("header"));

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "value"),
                        row("header2", null),
                        row("header3", "value 2")
                )
        );

        Mockito.verify(validator).validate(Set.of("header"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header3", "value 2"));
    }

    private static List<String> row(String... strings) {
        final var list = new ArrayList<String>();
        Collections.addAll(list, strings);
        return list;
    }
}
