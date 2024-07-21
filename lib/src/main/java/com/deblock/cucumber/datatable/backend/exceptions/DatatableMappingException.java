package com.deblock.cucumber.datatable.backend.exceptions;

import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.datatable.exception.CellMappingException;
import com.deblock.cucumber.datatable.validator.DataTableDoesNotMatch;

public class DatatableMappingException extends RuntimeException {
    public DatatableMappingException(Class<?> dataTableClass, Throwable cause) {
        super("Could not transform datatable to type " + dataTableClass + "\n" + cause.getMessage(), getSourceCause(cause));
        this.setStackTrace(getSourceStack(cause));
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    private static Throwable getSourceCause(Throwable cause) {
        if (cause == null) {
            return null;
        }
        if (!(cause instanceof DataTableDoesNotMatch) && !(cause instanceof CellMappingException) && !(cause instanceof TypeMetadata.ConversionError)) {
            return cause;
        }
        return getSourceCause(cause.getCause());
    }

    private static StackTraceElement[] getSourceStack(Throwable cause) {
        Throwable sourceCause = getSourceCause(cause);
        return sourceCause == null ? new StackTraceElement[0] : sourceCause.getStackTrace();
    }
}
