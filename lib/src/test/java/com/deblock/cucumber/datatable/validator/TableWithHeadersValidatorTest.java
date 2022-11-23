package com.deblock.cucumber.datatable.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.deblock.cucumber.datatable.validator.builder.HeaderBuilder.header;
import static com.deblock.cucumber.datatable.validator.builder.HeaderBuilder.typeMetadata;

public class TableWithHeadersValidatorTest {

    @Test
    public void should_return_error_when_column_does_not_exists() {
        final var validator = new DataTableValidator(List.of(
                header("header1").build(),
                header("header2").build()
        ));
        final var dataTableHeaders = List.of("headerUnknown");

        final var exception = Assertions.assertThrows(
                DataTableDoesNotMatch.class,
                () -> validator.validate(dataTableHeaders)
        );
        Assertions.assertEquals(
                "The following headers \"headerUnknown\" are not defined for this dataTable. Available headers are \"header1\", \"header2\"\n" + validator.description(),
                exception.getMessage()
        );
    }

    @Test
    public void should_return_error_when_missing_mandatory_column() {
        final var validator = new DataTableValidator(List.of(
                header("header1").build(),
                header("header2").build(),
                header("header3").mandatory().build()
        ));
        final var dataTableHeaders = List.of("header1");

        final var exception = Assertions.assertThrows(
                DataTableDoesNotMatch.class,
                () -> validator.validate(dataTableHeaders)
        );
        Assertions.assertEquals("The following headers are mandatory : \"header3\"\n" + validator.description(), exception.getMessage());
    }

    @Test
    public void should_not_return_error_when_mandatory_parameter_are_set() {
        final var validator = new DataTableValidator(List.of(
                header("header1").build(),
                header("header2").build(),
                header("header3").mandatory().build()
        ));
        final var dataTableHeaders = List.of("header3");

        Assertions.assertDoesNotThrow(() -> validator.validate(dataTableHeaders));
    }

    @Test
    public void can_use_alias() {
        final var validator = new DataTableValidator(List.of(
                header("header1").build(),
                header("header2").build(),
                header("header3", "alias1", "alias2").build()
        ));
        final var dataTableHeaders = List.of("alias2");

        Assertions.assertDoesNotThrow(() -> validator.validate(dataTableHeaders));
    }

    @Test
    public void description_contains_description_and_mandatory_information() {
        final var validator = new DataTableValidator(List.of(
                header("header1").description("the header 1").build(),
                header("h2").build(),
                header("header3", "alias1", "alias2").mandatory().description("the header 3").build()
        ));

        final var result = validator.description();

        final var expectedDescription =
                "| header1 | h2     | header3 |\n" +
                "| string  | string | string  |\n" +
                "\n" +
                "header1 (optional, type: string): the header 1\n" +
                "h2 (optional, type: string)\n" +
                "header3 (mandatory, type: string): the header 3\n";
        Assertions.assertEquals(expectedDescription, result);
    }

    @Test
    public void description_contains_type_and_default_from_type_metadata() {
        final var validator = new DataTableValidator(List.of(
                header("header1").description("the header 1").typeMetadata(typeMetadata("10", "integer")).defaultValue("15").build(),
                header("h2").typeMetadata(typeMetadata("motherDTO", "MotherDTO")).build(),
                header("header3", "alias1", "alias2").mandatory().description("the header 3").build()
        ));

        final var result = validator.description();

        final var expectedDescription = """
                | header1 | h2        | header3 |
                | 10      | motherDTO | string  |

                header1 (optional, default: 15, type: integer): the header 1
                h2 (optional, type: MotherDTO)
                header3 (mandatory, type: string): the header 3
                """;
        Assertions.assertEquals(expectedDescription, result);
    }

}
