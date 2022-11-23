package com.deblock.cucumber.datatable.data;

public interface TypeMetadata {
    String getTypeDescription();

    String getSample();

    Object convert(String value) throws ConversionError;

    class ConversionError extends RuntimeException {
        public ConversionError(String message, Throwable cause) {
            super(message, cause);
        }

        public ConversionError(String message) {
            super(message);
        }
    }
}
