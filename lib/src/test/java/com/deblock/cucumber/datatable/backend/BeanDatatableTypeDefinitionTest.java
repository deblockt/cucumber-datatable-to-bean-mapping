package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.backend.exceptions.DatatableMappingException;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.mapper.datatable.BeanDatatableMapper;
import com.deblock.cucumber.datatable.mapper.datatable.ColumnName;
import com.deblock.cucumber.datatable.validator.DataTableDoesNotMatch;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.CucumberInvocationTargetException;
import io.cucumber.datatable.CucumberDataTableException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BeanDatatableTypeDefinitionTest {

    @Test
    public void shouldValidateHeadersWithTableWithOneRow() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        when(beanMapper.headers()).thenReturn(List.of(
                new DatatableHeader(new ColumnName("header"), null, false, null, null),
                new DatatableHeader(new ColumnName("header2"), null, false, null, null)
        ));
        final var typeDefinition = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "header2"),
                        row("value", "value2")
                )
        );

        verify(validator).validate(Set.of("header", "header2"));
        verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
    }

    @Test
    public void shouldValidateUsingRowHeaderWhenNoMatchAndMoreThanTwoColumns() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        when(beanMapper.headers()).thenReturn(List.of(
                new DatatableHeader(new ColumnName("header"), null, false, null, null),
                new DatatableHeader(new ColumnName("header2"), null, false, null, null),
                new DatatableHeader(new ColumnName("header3"), null, false, null, null)
        ));
        final var typeDefinition = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("headerg", "header2g", "foo"),
                        row("value", "value2", "foo")
                )
        );

        verify(validator).validate(Set.of("headerg", "header2g", "foo"));
    }

    @Test
    public void shouldValidateUsingColumnHeaderWhenNoMatchAndOnlyTwoColumns() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        when(beanMapper.headers()).thenReturn(List.of(
                new DatatableHeader(new ColumnName("header"), null, false, null, null),
                new DatatableHeader(new ColumnName("header2"), null, false, null, null),
                new DatatableHeader(new ColumnName("header3"), null, false, null, null)
        ));
        final var typeDefinition = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("headerg", "value1"),
                        row("header2g", "value2")
                )
        );

        verify(validator).validate(Set.of("headerg", "header2g"));
    }

    @Test
    public void shouldValidateHeadersWithTwoColumn() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        when(beanMapper.headers()).thenReturn(List.of(
                new DatatableHeader(new ColumnName("header"), null, false, null, null),
                new DatatableHeader(new ColumnName("header2"), null, false, null, null)
        ));
        final var typeDefinition = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "value"),
                        row("header2", "value2")
                )
        );

        verify(validator).validate(Set.of("header", "header2"));
        verify(beanMapper).convert(Map.of("header", "value", "header2", "value2"));
    }

    @Test
    public void shouldRemoveNullColumns() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        final var typeDefinition = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);
        when(beanMapper.headers()).thenReturn(List.of(
                new DatatableHeader(new ColumnName("header"), null, false, null, null),
                new DatatableHeader(new ColumnName("header2"), null, false, null, null),
                new DatatableHeader(new ColumnName("header3"), null, false, null, null)
        ));

        typeDefinition.dataTableType().transform(
                List.of(
                        row("header", "value"),
                        row("header2", null),
                        row("header3", "value 2")
                )
        );

        verify(validator).validate(Set.of("header", "header3"));
        verify(beanMapper).convert(Map.of("header", "value", "header3", "value 2"));
    }

    @Test
    public void shouldMapExceptionWhenValidationFailed() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanDatatableMapper.class);
        Mockito.doThrow(new DataTableDoesNotMatch("error")).when(validator).validate(any());
        final var typeDefinition = new BeanDatatableTypeDefinition(Bean.class, validator, beanMapper);
        when(beanMapper.headers()).thenReturn(List.of(
                new DatatableHeader(new ColumnName("header"), null, false, null, null),
                new DatatableHeader(new ColumnName("header2"), null, false, null, null),
                new DatatableHeader(new ColumnName("header3"), null, false, null, null)
        ));

        final var dataTable = List.of(
                row("header", "value"),
                row("header2", null),
                row("header3", "value 2")
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
