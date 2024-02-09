package com.deblock.cucumber.datatable.mapper.typemetadata.exceptions;

import java.lang.reflect.Type;

public class NoConverterFound extends RuntimeException {

    public NoConverterFound(Type type) {
        super("can not find any converter for class " + type + ". You can define your own converter using @CustomDatatableFieldMapper");
    }
}
