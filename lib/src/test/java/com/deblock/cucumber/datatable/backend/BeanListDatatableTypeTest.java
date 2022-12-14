package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.BeanMapper;
import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanListDatatableTypeTest {

    @Test
    public void shouldValidateHeadersWithTableWithOneRow() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanMapper.class);
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
        final var beanMapper = Mockito.mock(BeanMapper.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator, beanMapper);

        final var result = typeDefinition.dataTableType().transform(List.of());

        Assertions.assertThat(result).isEqualTo(List.of());
    }
    @Test
    public void shouldIgnoreNullValues() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var beanMapper = Mockito.mock(BeanMapper.class);
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

    private static List<String> row(String... strings) {
        final var list = new ArrayList<String>();
        Collections.addAll(list, strings);
        return list;
    }
}
