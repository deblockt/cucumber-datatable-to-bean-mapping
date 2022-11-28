package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.BeanMapper;
import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

public class BeanDatatableTypeDefinitionTest {

    @Test
    public void shouldValidateHeadersWithTableWithOneRow() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanMapper.class);
        final var typeDefinition  = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        List.of("header", "header2"),
                        List.of("value", "value2")
                )
        );

        Mockito.verify(validator).validate(List.of("header", "header2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
    }

    @Test
    public void shouldValidateHeadersWithTwoColumn() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanMapper.class);
        final var typeDefinition  = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);
        Mockito.doThrow(new RuntimeException("error")).when(validator).validate(List.of("header", "value"));

        typeDefinition.dataTableType().transform(
                List.of(
                        List.of("header", "value"),
                        List.of("header2", "value2")
                )
        );

        Mockito.verify(validator).validate(List.of("header", "header2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
    }
}
