package com.deblock.cucumber.datatable.mapper.datatable;


import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.data.TypeMetadata;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.mapper.TypeMetadataFactory;

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

    public SimpleColumnDatatableMapper(Column column, String name, Type genericType, TypeMetadataFactory typeMetadataFactory) {
        this.typeMetadata = typeMetadataFactory.build(genericType);
        this.header = new DatatableHeader(column, name, typeMetadata);
    }

    @Override
    public List<DatatableHeader> headers() {
        return List.of(this.header);
    }

    @Override
    public Object convert(Map<String, String> entry) {
        for (final var headerName : this.header.names()) {
            if (entry.containsKey(headerName)) {
                return this.typeMetadata.convert(entry.get(headerName));
            }
        }
        if (this.header.defaultValue() == null) {
            return null;
        }
        return this.typeMetadata.convert(header.defaultValue());
    }
}