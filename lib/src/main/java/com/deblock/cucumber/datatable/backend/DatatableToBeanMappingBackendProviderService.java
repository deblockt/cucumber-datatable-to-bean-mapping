package com.deblock.cucumber.datatable.backend;

import io.cucumber.core.backend.Backend;
import io.cucumber.core.backend.BackendProviderService;
import io.cucumber.core.backend.Container;
import io.cucumber.core.backend.Lookup;

import java.util.function.Supplier;

public class DatatableToBeanMappingBackendProviderService implements BackendProviderService {
    @Override
    public Backend create(Lookup lookup, Container container, Supplier<ClassLoader> classLoader) {
        return new DatatableToBeanMappingBanckend(classLoader);
    }
}
