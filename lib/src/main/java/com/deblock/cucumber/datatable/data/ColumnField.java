package com.deblock.cucumber.datatable.data;

import java.util.List;
import java.util.Map;

/**
 * This interface represent a field annotated with @Column
 */
public interface ColumnField {
    /**
     * @return list of headers represented by the field
     */
    List<DatatableHeader> headers();

    /**
     * this method to read the column on the entry and convert it on object representation
     *
     * @param entry the cucumber entry
     *
     * @return the object representation of the column
     */
    Object convert(Map<String, String> entry);
}
