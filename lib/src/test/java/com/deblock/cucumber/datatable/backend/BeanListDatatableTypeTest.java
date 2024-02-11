package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.backend.exceptions.DatatableMappingException;
import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.validator.DataTableDoesNotMatch;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.CucumberInvocationTargetException;
import io.cucumber.datatable.CucumberDataTableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class BeanListDatatableTypeTest {

    @Test
    public void shouldValidateHeadersWithTableWithOneRow() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "header2"),
                        row("value", "value2"),
                        row("value3", "value4")
                )
        );

        Mockito.verify(validator, Mockito.times(2)).validate(Set.of("header", "header2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value3", "header2", "value4"));
    }

    @Test
    public void shouldReturnErrorWhenHeadersAreNotPresent() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator, beanMapper);

        final var result = typeDefinition.dataTableType().transform(List.of());

        Assertions.assertThat(result).isEqualTo(List.of());
    }
    @Test
    public void shouldIgnoreNullValues() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "header2"),
                        row("value", "value2"),
                        row("value3", null)
                )
        );

        Mockito.verify(validator).validate(Set.of("header", "header2"));
        Mockito.verify(validator).validate(Set.of("header"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
        Mockito.verify(beanMapper).convert(Map.of("header", "value3"));
    }

    @Test
    public void shouldMapExceptionWhenValidationFailed() {
        final var validator = Mockito.mock(DataTableValidator.class);
        Mockito.doThrow(new DataTableDoesNotMatch("error")).when(validator).validate(any());
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator, beanMapper);

        final var dataTable = List.of(
                row("header", "header2"),
                row("value", "value2"),
                row("value3", null)
        );
        final Exception result = assertThrows(CucumberDataTableException.class, () -> typeDefinition.dataTableType().transform(dataTable));

        assertThat(result).hasCauseInstanceOf(CucumberInvocationTargetException.class);
        final CucumberInvocationTargetException cause = ((CucumberInvocationTargetException)result.getCause());
        assertThat(cause.getInvocationTargetExceptionCause()).isInstanceOf(DatatableMappingException.class);
        assertThat(cause.getInvocationTargetExceptionCause()).hasStackTraceContaining("Could not transform datatable to type class com.deblock.cucumber.datatable.mapper.beans.Bean\nerror");
    }

    private static List<String> row(String... strings) {
        final var list = new ArrayList<String>();
        Collections.addAll(list, strings);
        return list;
    }
}
