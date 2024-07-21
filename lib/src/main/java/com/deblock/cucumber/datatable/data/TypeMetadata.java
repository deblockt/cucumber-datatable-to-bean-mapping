package com.deblock.cucumber.datatable.data;

/**
 * Interface to implement a converter to convert a string to a type
 * This is used to convert a cell value to an object value
 */
public interface TypeMetadata {
    String typeDescription();

    String sample();

    Object convert(String value) throws ConversionError;

    class ConversionError extends RuntimeException {
        public ConversionError(String message, Throwable cause) {
            super(message, cause);
            this.setStackTrace(cause.getStackTrace());
        }

        public ConversionError(String message) {
            super(message);
        }
    }
}
