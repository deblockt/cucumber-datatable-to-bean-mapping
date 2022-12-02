package com.deblock.cucumber.datatable.mapper.typemetadata.custom;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import com.deblock.cucumber.datatable.backend.DatatableToBeanMappingBackend;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.core.resource.ClasspathScanner;
import io.cucumber.core.resource.ClasspathSupport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static io.cucumber.core.resource.ClasspathSupport.CLASSPATH_SCHEME;
import static io.cucumber.core.resource.ClasspathSupport.classPathScanningExplanation;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

public class CustomTypeMetadataFactory implements TypeMetadataFactory {
    private static final Logger log = LoggerFactory.getLogger(DatatableToBeanMappingBackend.class);

    private final Map<Class<?>, TypeMetadata> customTypes = new HashMap<>();

    public CustomTypeMetadataFactory(ClasspathScanner classFinder, List<URI> gluePaths) {
        gluePaths.stream()
                .filter(gluePath -> CLASSPATH_SCHEME.equals(gluePath.getScheme()))
                .map(ClasspathSupport::packageName)
                .map(classFinder::scanForClassesInPackage)
                .flatMap(Collection::stream)
                .distinct()
                .forEach(aGlueClass -> scan(aGlueClass, (method, annotation) -> {
                    if (CustomDatatableFieldMapper.class.equals(annotation.annotationType())) {
                        this.addCustomType(method.getReturnType(), new CustomJavaBeanTypeMetadata(method, (CustomDatatableFieldMapper) annotation));
                    }
                }));
    }

    @Override
    public TypeMetadata build(Type type) {
        return customTypes.get(type);
    }

    public void addCustomType(Class<?> clazz, TypeMetadata typeMetadata) {
        this.customTypes.put(clazz, typeMetadata);
    }

    static void scan(Class<?> aClass, BiConsumer<Method, Annotation> consumer) {
        // prevent unnecessary checking of Object methods
        if (Object.class.equals(aClass)) {
            return;
        }

        if (!isInstantiable(aClass)) {
            return;
        }
        for (Method method : safelyGetMethods(aClass)) {
            scan(consumer, aClass, method);
        }
    }

    private static void scan(BiConsumer<Method, Annotation> consumer, Class<?> aClass, Method method) {
        // prevent unnecessary checking of Object methods
        if (Object.class.equals(method.getDeclaringClass())) {
            return;
        }
        scan(consumer, aClass, method, method.getAnnotations());
    }

    private static void scan(
            BiConsumer<Method, Annotation> consumer, Class<?> aClass, Method method, Annotation[] methodAnnotations
    ) {
        for (Annotation annotation : methodAnnotations) {
            consumer.accept(method, annotation);
        }
    }

    private static boolean isInstantiable(Class<?> clazz) {
        return isPublic(clazz.getModifiers())
                && !isAbstract(clazz.getModifiers())
                && (isStatic(clazz.getModifiers()) || clazz.getEnclosingClass() == null);
    }

    private static Method[] safelyGetMethods(Class<?> aClass) {
        try {
            return aClass.getMethods();
        } catch (NoClassDefFoundError e) {
            log.warn(e, () -> "Failed to load methods of class '" + aClass.getName() + "'.\n" + classPathScanningExplanation());
        }
        return new Method[0];
    }
}
