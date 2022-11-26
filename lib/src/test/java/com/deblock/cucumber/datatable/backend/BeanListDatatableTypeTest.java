package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.mapper.beans.Bean;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.datatable.CucumberDataTableException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

public class BeanListDatatableTypeTest {

    @Test
    public void shouldValidateHeadersWithTableWithOneRow() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator);

        typeDefinition.dataTableType().transform(
                List.of(
                        List.of("header", "header2"),
                        List.of("value", "value2"),
                        List.of("value3", "value4")
                )
        );

        Mockito.verify(validator, Mockito.times(2)).validate(Set.of("header", "header2"));
    }

    @Test
    public void shouldReturnErrorWhenHeadersAreNotPresent() {
        final var validator = Mockito.mock(DataTableValidator.class);
        final var typeDefinition = new BeanListDatatableTypeDefinition(Bean.class, validator);

        final var result = typeDefinition.dataTableType().transform(List.of());

        Assertions.assertThat(result).isEqualTo(List.of());
    }
}
