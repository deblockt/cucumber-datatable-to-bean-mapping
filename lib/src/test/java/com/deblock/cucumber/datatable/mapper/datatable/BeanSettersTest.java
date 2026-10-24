package com.deblock.cucumber.datatable.mapper.datatable;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanSettersTest {

    @Test
    public void shouldBuildSetterNameFromFieldName() throws NoSuchFieldException {
        assertThat(BeanSetters.setterName(field(BeanWithSetters.class, "name"))).isEqualTo("setName");
        assertThat(BeanSetters.setterName(field(BeanWithSetters.class, "x"))).isEqualTo("setX");
    }

    @Test
    public void shouldFindPublicSetter() throws NoSuchFieldException {
        final var setter = BeanSetters.findSetter(BeanWithSetters.class, field(BeanWithSetters.class, "name"));

        assertThat(setter).hasValueSatisfying(method -> {
            assertThat(method.getName()).isEqualTo("setName");
            assertThat(method.getParameterTypes()).containsExactly(String.class);
        });
    }

    @Test
    public void shouldFindSetterDeclaredOnParentClass() throws NoSuchFieldException {
        final var setter = BeanSetters.findSetter(ChildBean.class, field(BeanWithSetters.class, "name"));

        assertThat(setter).hasValueSatisfying(method -> assertThat(method.getName()).isEqualTo("setName"));
    }

    @Test
    public void shouldNotFindMissingSetter() throws NoSuchFieldException {
        final var setter = BeanSetters.findSetter(BeanWithSetters.class, field(BeanWithSetters.class, "withoutSetter"));

        assertThat(setter).isEmpty();
    }

    @Test
    public void shouldNotFindPrivateSetter() throws NoSuchFieldException {
        final var setter = BeanSetters.findSetter(BeanWithSetters.class, field(BeanWithSetters.class, "withPrivateSetter"));

        assertThat(setter).isEmpty();
    }

    @Test
    public void shouldNotFindSetterWithMoreThanOneParameter() throws NoSuchFieldException {
        final var setter = BeanSetters.findSetter(BeanWithSetters.class, field(BeanWithSetters.class, "withTwoParametersSetter"));

        assertThat(setter).isEmpty();
    }

    private static Field field(Class<?> clazz, String name) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    public static class BeanWithSetters {
        private String name;
        private int x;
        private String withoutSetter;
        private String withPrivateSetter;
        private String withTwoParametersSetter;

        public void setName(String name) {
            this.name = name;
        }

        public void setX(int x) {
            this.x = x;
        }

        private void setWithPrivateSetter(String value) {
            this.withPrivateSetter = value;
        }

        public void setWithTwoParametersSetter(String value, String other) {
            this.withTwoParametersSetter = value + other;
        }
    }

    public static class ChildBean extends BeanWithSetters {
    }
}
