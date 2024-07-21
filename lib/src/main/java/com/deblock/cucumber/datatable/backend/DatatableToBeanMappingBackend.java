package com.deblock.cucumber.datatable.backend;

import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;
import com.deblock.cucumber.datatable.backend.options.FullOptions;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.GenericMapperFactory;
import com.deblock.cucumber.datatable.mapper.MapperFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.CompositeTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.collections.CollectionTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.custom.CustomTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.date.TemporalTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.enumeration.EnumTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.map.MapTypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.typemetadata.primitive.PrimitiveTypeMetadataFactoryImpl;
import com.deblock.cucumber.datatable.runtime.ColumnNameBuilderServiceLoader;
import com.deblock.cucumber.datatable.runtime.DateTimeServiceLoader;
import com.deblock.cucumber.datatable.runtime.FieldResolverServiceLoader;
import com.deblock.cucumber.datatable.validator.DataTableValidator;
import io.cucumber.core.backend.Backend;
import io.cucumber.core.backend.Glue;
import io.cucumber.core.backend.Lookup;
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
    private final FieldResolverServiceLoader fieldResolveServiceLoader;
    private final DateTimeServiceLoader dateTimeServiceLoader;
    private final Lookup lookup;

    public DatatableToBeanMappingBackend(Supplier<ClassLoader> classLoaderSupplier, FullOptions options, Lookup lookup) {
        this.classFinder = new ClasspathScanner(classLoaderSupplier);
        final var columnNameBuilderServiceLoader = new ColumnNameBuilderServiceLoader(options, classLoaderSupplier);
        this.fieldResolveServiceLoader = new FieldResolverServiceLoader(options, classLoaderSupplier, columnNameBuilderServiceLoader);
        this.dateTimeServiceLoader = new DateTimeServiceLoader(options, classLoaderSupplier);
        this.lookup = lookup;
    }

    @Override
    public void loadGlue(Glue glue, List<URI> gluePaths) {
        final var customTypeMetadataFactory = new CustomTypeMetadataFactory(this.classFinder, gluePaths, lookup);
        final var typeMetadataFactory = new CompositeTypeMetadataFactory(
            customTypeMetadataFactory,
            new PrimitiveTypeMetadataFactoryImpl(),
            new TemporalTypeMetadataFactory(dateTimeServiceLoader.loadService()),
            new EnumTypeMetadataFactory(),
            new MapTypeMetadataFactory()
        );
        typeMetadataFactory.add(new CollectionTypeMetadataFactory(typeMetadataFactory));
        final var mapperFactory = new GenericMapperFactory(typeMetadataFactory, fieldResolveServiceLoader.loadService());

        gluePaths.stream()
                .filter(gluePath -> CLASSPATH_SCHEME.equals(gluePath.getScheme()))
                .map(ClasspathSupport::packageName)
                .map(classFinder::scanForClassesInPackage)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(aGlueClass -> {
                    final var annotation = aGlueClass.getAnnotation(DataTableWithHeader.class);
                    if (annotation != null) {
                        registerDataTableDefinition(glue, aGlueClass, mapperFactory);
                    }
                });
    }

    private void registerDataTableDefinition(Glue glue, Class<?> aGlueClass, MapperFactory mapperFactory) {
        DatatableMapper datatableMapper = mapperFactory.build(aGlueClass);
        final var validator = new DataTableValidator(datatableMapper.headers(), false);
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
