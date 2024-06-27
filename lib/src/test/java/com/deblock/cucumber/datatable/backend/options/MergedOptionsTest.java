package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.UseFieldNameColumnNameBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class MergedOptionsTest {

    @Test
    public void getNameBuilderClassShouldReturnNullWhenNoOneHasTheProperty() {
        final var option1 = Mockito.mock(FullOptions.class);
        final var option2 = Mockito.mock(FullOptions.class);
        final var mergedOptions = new MergedOptions(
                option1, option2
        );

        final var result = mergedOptions.getNameBuilderClass();

        assertThat(result).isNull();
    }

    @Test
    public void getNameBuilderClassShouldReturnTheFirstValueNonNull() {
        final var option1 = Mockito.mock(FullOptions.class);
        final var option2 = Mockito.mock(FullOptions.class);
        final var option3 = Mockito.mock(FullOptions.class);
        final var mergedOptions = new MergedOptions(
                option1, option2, option3
        );
        Mockito.when(option2.getNameBuilderClass()).thenReturn((Class) HumanReadableColumnNameBuilder.class);
        Mockito.when(option3.getNameBuilderClass()).thenReturn((Class) UseFieldNameColumnNameBuilder.class);

        final var result = mergedOptions.getNameBuilderClass();

        assertThat(result).isEqualTo(HumanReadableColumnNameBuilder.class);
    }
}
