package com.deblock.cucumber.datatable.backend;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Define its own copy of one class instead of asking its parent, like a test class loader that is separate from the lib's one.
 */
public class ChildFirstClassLoader extends ClassLoader {
    private final String childFirstClassName;

    public ChildFirstClassLoader(String childFirstClassName, ClassLoader parent) {
        super(parent);
        this.childFirstClassName = childFirstClassName;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (!name.equals(childFirstClassName)) {
            return super.loadClass(name, resolve);
        }
        synchronized (getClassLoadingLock(name)) {
            final var loaded = findLoadedClass(name);
            if (loaded != null) {
                return loaded;
            }
            final var resource = name.replace('.', '/') + ".class";
            try (var stream = getParent().getResourceAsStream(resource)) {
                final var bytes = stream.readAllBytes();
                return defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
