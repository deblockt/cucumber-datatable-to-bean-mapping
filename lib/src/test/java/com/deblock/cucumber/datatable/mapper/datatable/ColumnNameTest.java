package com.deblock.cucumber.datatable.mapper.datatable;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ColumnNameTest {

    @Nested
    public class AddChild {

        @Test
        public void shouldReturnChildOnEmptyName() {
            final var name = new ColumnName();

            final var result = name.addChild(new ColumnName("name1", "name 2"));

            assertThat(result).isEqualTo(new ColumnName("name1", "name 2"));
        }

        @Test
        public void shouldAddNameOnMultiName() {
            final var name = new ColumnName("parent1", "parent2");

            final var result = name.addChild(new ColumnName("name1", "<parent_name> name 2"));

            assertThat(result).isEqualTo(new ColumnName("name1", "parent1 name 2", "parent2 name 2"));
        }
    }

    @Nested
    public class Contains {

        @Test
        public void shouldReturnTrueIfNameIsContained() {
            final var name = new ColumnName("name1", "name 2");

            final var result = name.contains("name 2");

            assertThat(result).isTrue();
        }

        @Test
        public void shouldReturnFalseIfNameIsNotContained() {
            final var name = new ColumnName("name1", "name 2");

            final var result = name.contains("name 10");

            assertThat(result).isFalse();
        }
    }

    @Nested
    public class FirstName {

        @Test
        public void shouldReturnTheFirstNameOfTheList() {
            final var name = new ColumnName("name1", "name 2");

            final var result = name.firstName();

            assertThat(result).isEqualTo("name1");
        }

        @Test
        public void shouldReturnEmptyStringIfNoName() {
            final var name = new ColumnName();

            final var result = name.firstName();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    public class HasOneNameEquals {

        @Test
        public void shouldReturnTrueIfOneNameEquals() {
            final var name = new ColumnName("name1", "name 2");

            final var result = name.hasOneNameEquals(new ColumnName("name 1", "name 2"));

            assertThat(result).isTrue();
        }

        @Test
        public void shouldReturnFalseIfNoNameEquals() {
            final var name = new ColumnName("name1", "name 2");

            final var result = name.hasOneNameEquals(new ColumnName("name 1", "name 20"));

            assertThat(result).isFalse();
        }
    }

    @Nested
    public class Iterator {
        @Test
        public void canIterateOverNames() {
            final var columnName = new ColumnName("name1", "name3");

            final var result = new ArrayList<>();
            for (String name: columnName) {
                result.add(name);
            }
            assertThat(result).isEqualTo(List.of("name1", "name3"));
        }
    }

    @Nested
    public class ToString {
        @Test
        public void canCallToString() {
            final var columnName = new ColumnName("name1", "name3");

            final var result = columnName.toString();

            assertThat(result).isEqualTo("name1,name3");
        }
    }

    @Nested
    public class Merge {
        @Test
        public void shouldMergeAndDistinct() {
            final var columnName1 = new ColumnName("name1", "name3");
            final var columnName2 = new ColumnName("name2", "name4", "name3");

            final var result = ColumnName.merge(List.of(columnName1, columnName2));

            assertThat(result).isEqualTo(new ColumnName("name1", "name3", "name2", "name4"));
        }
    }
}
