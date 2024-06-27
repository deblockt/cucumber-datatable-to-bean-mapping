package com.deblock.cucumber.datatable.mapper.datatable;

import com.deblock.cucumber.datatable.data.DatatableHeader;
import com.deblock.cucumber.datatable.mapper.DatatableMapper;
import com.deblock.cucumber.datatable.validator.DataTableDoesNotMatch;
import com.deblock.cucumber.datatable.validator.DataTableValidator;

import java.util.List;
import java.util.Map;

public class ChildObjectDatatableMapper implements DatatableMapper {

    private final DatatableMapper objectMapper;
    private final FieldResolver.FieldInfo fieldInfo;
    private final DataTableValidator validator;

    public ChildObjectDatatableMapper(FieldResolver.FieldInfo fieldInfo, DatatableMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.fieldInfo = fieldInfo;
        this.validator = new DataTableValidator(objectMapper.headers(), true);
    }

    @Override
    public List<DatatableHeader> headers() {
        if (!this.fieldInfo.optional()) {
            return this.objectMapper.headers();
        }
        return this.objectMapper.headers()
                .stream()
                .map(header -> new DatatableHeader(
                        header.names(),
                        header.description(),
                        true,
                        header.defaultValue(),
                        header.typeMetadata()
                ))
                .toList();
    }

    @Override
    public Object convert(Map<String, String> entry) {
        try {
            this.validator.validate(entry.keySet());
            return objectMapper.convert(entry);
        } catch (DataTableDoesNotMatch ex) {
            return null;
        }
    }
}
