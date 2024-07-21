package com.deblock.cucumber.datatable.backend.options;

import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver;
import com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.ImplicitFieldResolver;
import com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.name.UseFieldNameColumnNameBuilder;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MergedOptionsTest {

    @Nested
    class GetNameBuilderClassMethod {
        @Test
        void shouldReturnNullWhenNoOneHasTheProperty() {
            final var option1 = Mockito.mock(FullOptions.class);
            final var option2 = Mockito.mock(FullOptions.class);
            final var mergedOptions = new MergedOptions(
                    option1, option2
            );

            final var result = mergedOptions.getNameBuilderClass();

            assertThat(result).isNull();
        }

        @Test
        void shouldReturnTheFirstValueNonNull() {
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

    @Nested
    class GetFieldResolverClassMethod {
        @Test
        void shouldReturnNullWhenNoOneHasTheProperty() {
            final var option1 = Mockito.mock(FullOptions.class);
            final var option2 = Mockito.mock(FullOptions.class);
            final var mergedOptions = new MergedOptions(
                    option1, option2
            );

            final var result = mergedOptions.getFieldResolverClass();

            assertThat(result).isNull();
        }

        @Test
        void shouldReturnTheFirstValueNonNull() {
            final var option1 = Mockito.mock(FullOptions.class);
            final var option2 = Mockito.mock(FullOptions.class);
            final var option3 = Mockito.mock(FullOptions.class);
            final var mergedOptions = new MergedOptions(
                    option1, option2, option3
            );
            Mockito.when(option2.getFieldResolverClass()).thenReturn((Class) ImplicitFieldResolver.class);
            Mockito.when(option3.getFieldResolverClass()).thenReturn((Class) DeclarativeFieldResolver.class);

            final var result = mergedOptions.getFieldResolverClass();

            assertThat(result).isEqualTo(ImplicitFieldResolver.class);
        }
    }

    @Nested
    class GetDateTimeServiceClassMethod {
        @Test
        void shouldReturnNullWhenNoOneHasTheProperty() {
            final var option1 = Mockito.mock(FullOptions.class);
            final var option2 = Mockito.mock(FullOptions.class);
            final var mergedOptions = new MergedOptions(
                    option1, option2
            );

            final var result = mergedOptions.getDateTimeServiceClass();

            assertThat(result).isNull();
        }

        @Test
        void shouldReturnTheFirstValueNonNull() {
            final var option1 = Mockito.mock(FullOptions.class);
            final var option2 = Mockito.mock(FullOptions.class);
            final var option3 = Mockito.mock(FullOptions.class);
            final var mergedOptions = new MergedOptions(
                    option1, option2, option3
            );
            Mockito.when(option2.getDateTimeServiceClass()).thenReturn((Class) StaticDateTimeService.class);
            Mockito.when(option3.getDateTimeServiceClass()).thenReturn((Class) TestDateTimeService.class);

            final var result = mergedOptions.getDateTimeServiceClass();

            assertThat(result).isEqualTo(StaticDateTimeService.class);
        }
    }

    class TestDateTimeService implements DateTimeService {
        @Override
        public OffsetDateTime now() {
            return null;
        }
    }
}
