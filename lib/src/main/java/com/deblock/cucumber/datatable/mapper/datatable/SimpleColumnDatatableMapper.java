package com.deblock.cucumber.datatable.mapper.datatable;


import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;
import com.deblock.cucumber.datatable.mapper.datatable.exception.CellMappingException;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Class to convert a field annotated with @Column and using a primitive type
 * The datatable value should be contained on one column
 */
public class SimpleColumnDatatableMapper implements DatatableMapper {
    private final TypeMetadata typeMetadata;
    private final DatatableHeader header;

    public SimpleColumnDatatableMapper(Column column, ColumnName columnName, Type genericType, TypeMetadataFactory typeMetadataFactory) {
        this.typeMetadata = typeMetadataFactory.build(genericType);
        this.header = new DatatableHeader(column, columnName, typeMetadata);
    }

    @Override
    public List<DatatableHeader> headers() {
        return List.of(this.header);
    }

    @Override
    public Object convert(Map<String, String> entry) {
        for (final var headerName : this.header.names()) {
            if (entry.containsKey(headerName)) {
                return convertValue(entry.get(headerName), headerName);
            }
        }
        if (this.header.defaultValue() == null) {
            return null;
        }
        return convertValue(header.defaultValue(), this.header.names().firstName());
    }

    private Object convertValue(String entry, String headerName) {
        try {
            return this.typeMetadata.convert(entry);
        } catch (Exception e) {
            throw new CellMappingException(headerName, entry, this.typeMetadata.typeDescription(), e);
        }
    }
}
