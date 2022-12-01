package com.deblock.cucumber.datatable.mapper;

public class RegularClassOrRecordMapperFactory implements MapperFactory {
    @Override
    public DatatableMapper build(Class<?> clazz, TypeMetadataFactory typeMetadataFactory) {
        if (clazz.isRecord()) {
            return new RecordMapper(clazz, typeMetadataFactory);
        } else {
            return new BeanMapper(clazz, typeMetadataFactory);
        }
    }
}
