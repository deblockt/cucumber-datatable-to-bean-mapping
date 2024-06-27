package com.deblock.cucumber.datatable.mapper.datatable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ColumnName implements Iterable<String> {

    private final List<String> names;

    public ColumnName(List<String> names) {
        this.names = names;
    }

    public ColumnName() {
        this(List.of());
    }

    public ColumnName(String ...names) {
        this.names = Arrays.asList(names);
    }

    public static ColumnName merge(List<ColumnName> names) {
        return new ColumnName(
                names.stream()
                .flatMap(it -> it.names.stream())
                .distinct()
                .toList()
        );
    }

    public ColumnName addChild(List<String> childNames) {
        if (names.isEmpty()) {
            return new ColumnName(childNames);
        }
        final List<String> replacedNames = childNames.stream()
                .flatMap(childName ->
                        this.names.stream()
                                .map(parentName -> childName.replace("<parent_name>", parentName))
                                .distinct()
                ).toList();
        return new ColumnName(replacedNames);
    }

    public boolean contains(String name) {
        return this.names.contains(name);
    }

    public String toString() {
        return String.join(",", this.names);
    }

    @Override
    public Iterator<String> iterator() {
        return this.names.iterator();
    }

    public String firstName() {
        return this.names.get(0);
    }

    public boolean hasOneNameEquals(ColumnName other) {
        return !Collections.disjoint(this.names, other.names);
    }
}
