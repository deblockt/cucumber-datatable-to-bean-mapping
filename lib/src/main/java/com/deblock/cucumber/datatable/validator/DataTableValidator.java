package com.deblock.cucumber.datatable.validator;

import com.deblock.cucumber.datatable.data.DatatableHeader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Allow to validate a dataTable using this format
 */
public class DataTableValidator {
    private final Collection<DatatableHeader> headers;

    public DataTableValidator(Collection<DatatableHeader> headers) {
        this.headers = headers;
    }

    public void validate(Collection<String> headers) {
        final var additionalHeaders = headers.stream()
                .filter(header -> findHeaderMatching(header).isEmpty())
                .collect(Collectors.toList());

        if (!additionalHeaders.isEmpty()) {
            throwUnknownHeaderException(additionalHeaders);
        }

        final var nonMatchMandatoryHeader = this.headers.stream()
                .filter(it -> !it.optional())
                .filter(header -> headers.stream().noneMatch(header::match))
                .map(it -> it.names().get(0))
                .collect(Collectors.joining("\", \""));
        if (nonMatchMandatoryHeader.length() > 0) {
            throw new DataTableDoesNotMatch(
                    "The following headers are mandatory : \"" + nonMatchMandatoryHeader + "\"\n" + this.description()
            );
        }
    }

    public String description() {
        final var stringBuilder = new StringBuilder();
        final var sample = new ArrayList<List<String>>();
        sample.add(this.headers.stream().map(it -> it.names().get(0)).collect(Collectors.toList()));
        sample.add(this.headers.stream().map(DatatableHeader::sample).collect(Collectors.toList()));

        new TablePrinter().printTable(sample, stringBuilder);

        stringBuilder.append('\n');

        for (DatatableHeader header : this.headers) {
            stringBuilder.append(header.names().get(0));
            stringBuilder.append(" (");
            stringBuilder.append(header.optional() ? "optional" : "mandatory");
            if (header.defaultValue() != null && !header.defaultValue().isBlank()) {
                stringBuilder.append(", default: ");
                stringBuilder.append(header.defaultValue());
            }

            stringBuilder.append(", type: ");
            stringBuilder.append(header.typeDescription());

            stringBuilder.append(")");
            if (header.description() != null && header.description().length() > 0) {
                stringBuilder.append(": ");
                stringBuilder.append(header.description());
            }
            stringBuilder.append('\n');
        }

        return stringBuilder.toString();
    }

    private void throwUnknownHeaderException(List<String> headers) {
        final var availableHeaders = this.headers.stream()
                .map(it -> it.names().get(0))
                .collect(Collectors.joining("\", \""));
        throw new DataTableDoesNotMatch(
                "The following headers \"" + String.join("\", \"", headers) + "\" are not defined for this dataTable. "
                        + "Available headers are \"" + availableHeaders + "\"\n" + this.description()
        );
    }

    private Optional<DatatableHeader> findHeaderMatching(String name) {
        return this.headers.stream()
                .filter(it -> it.match(name))
                .findFirst();
    }
}
