package com.deblock.cucumber.datatable;

import com.deblock.cucumber.datatable.backend.DatatableToBeanMappingBackend;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Cucumber internal packages (like its shaded Jackson) change between Cucumber versions.
 * As cucumber-core is supplied by the user, the lib must not reference them.
 */
class NoCucumberInternalDependencyTest {
    private static final Pattern CUCUMBER_INTERNAL_CLASS = Pattern.compile("io/cucumber/[\\w/$]*internal/[\\w/$]+");

    @Test
    void libClassesShouldNotReferenceCucumberInternalClasses() throws Exception {
        final var mainClassesDir = Path.of(DatatableToBeanMappingBackend.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        final List<String> violations;
        try (Stream<Path> files = Files.walk(mainClassesDir)) {
            violations = files
                    .filter(file -> file.toString().endsWith(".class"))
                    .flatMap(file -> CUCUMBER_INTERNAL_CLASS.matcher(readClassFile(file)).results()
                            .map(match -> mainClassesDir.relativize(file) + " -> " + match.group()))
                    .distinct()
                    .toList();
        }

        assertThat(violations).isEmpty();
    }

    private static String readClassFile(Path file) {
        try {
            // class names are stored as plain text in the class file constant pool
            return new String(Files.readAllBytes(file), StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
