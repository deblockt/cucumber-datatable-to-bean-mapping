package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.MapperFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.CompositeTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.collections.CollectionTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.custom.CustomTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticGetTimeService;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.TemporalTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.primitive.PrimitiveTypeMetadataFactoryImpl;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.Backend;
import io.cucumber.core.backend.Glue;
import io.cucumber.core.backend.Snippet;
import io.cucumber.core.resource.ClasspathScanner;
import io.cucumber.core.resource.ClasspathSupport;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static io.cucumber.core.resource.ClasspathSupport.CLASSPATH_SCHEME;

public class DatatableToBeanMappingBackend implements Backend {
    private final ClasspathScanner classFinder;
    private final MapperFactory mapperFactory;

    public DatatableToBeanMappingBackend(Supplier<ClassLoader> classLoaderSupplier, MapperFactory mapperFactory) {
        this.classFinder = new ClasspathScanner(classLoaderSupplier);
        this.mapperFactory = mapperFactory;
    }

    @Override
    public void loadGlue(Glue glue, List<URI> gluePaths) {
        gluePaths.stream()
                .filter(gluePath -> CLASSPATH_SCHEME.equals(gluePath.getScheme()))
                .map(ClasspathSupport::packageName)
                .map(classFinder::scanForClassesInPackage)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(aGlueClass -> {
                    final var annotation = aGlueClass.getAnnotation(DataTableWithHeader.class);
                    if (annotation != null) {
                        registerDataTableDefinition(glue, aGlueClass);
                    }
                });
    }

    private void registerDataTableDefinition(Glue glue, Class<?> aGlueClass) {
        final var typeMetadataFactory = new CompositeTypeMetadataFactory(
                CustomTypeMetadataFactory.INSTANCE,
                new PrimitiveTypeMetadataFactoryImpl(),
                new TemporalTypeMetadataFactory(new StaticGetTimeService())
        );
        typeMetadataFactory.add(new CollectionTypeMetadataFactory(typeMetadataFactory));


        DatatableMapper datatableMapper = this.mapperFactory.build(aGlueClass, typeMetadataFactory);
        final var validator = new DataTableValidator(datatableMapper.headers());
        glue.addDataTableType(new BeanDatatableTypeDefinition(aGlueClass, validator, datatableMapper));
        glue.addDataTableType(new BeanListDatatableTypeDefinition(aGlueClass, validator, datatableMapper));
    }

    @Override
    public void buildWorld() {

    }

    @Override
    public void disposeWorld() {

    }

    @Override
    public Snippet getSnippet() {
        return null;
    }
}
