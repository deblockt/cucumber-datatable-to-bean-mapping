package com.deblock.cucumber.datatable.mapper;

import com.deblock.cucumber.datatable.data.DatatableHeader;

import java.util.List;
import java.util.Map;

/**
 * This interface represent mapper reading a datatable a returning an object
 * It can be used to convert a field annotated with @Column
 * Or a class annotated with @DataTableWithHeader
 */
public interface DatatableMapper {
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
