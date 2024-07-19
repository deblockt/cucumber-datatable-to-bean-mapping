package com.deblock.cucumber.datatable.runtime;

import io.cucumber.core.exception.CucumberException;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public abstract class AbstractServiceLoader<T> {

    private final Supplier<ClassLoader> classLoaderSupplier;
    private final Class<T> serviceInterface;
    private final Class<? extends T> expectedClass;

    protected AbstractServiceLoader(
            Class<T> serviceInterface,
            Class<? extends T> expectedClass,
            Supplier<ClassLoader> classLoaderSupplier
    ) {
        this.serviceInterface = serviceInterface;
        this.expectedClass = expectedClass;
        this.classLoaderSupplier = classLoaderSupplier;
    }

    public T loadService() {
        ServiceLoader<T> loader = ServiceLoader.load(serviceInterface, classLoaderSupplier.get());

        for (T serviceClass : loader) {
            if (expectedClass.equals(serviceClass.getClass())) {
                return serviceClass;
            }
        }

        throw new CucumberException(
                """
                Could not find class %s.
                Cucumber uses SPI to discover %s implementations.
                Has the class been registered with SPI and is it available on the classpath?
                You should register the class %s on a file names META-INF/services/%s
                """.formatted(
                    expectedClass.getName(), serviceInterface.getSimpleName(),
                    expectedClass.getName(), serviceInterface.getName()
                )
        );
    }
}
