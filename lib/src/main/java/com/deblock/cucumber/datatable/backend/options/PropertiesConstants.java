package com.deblock.cucumber.datatable.backend.options;

public final class PropertiesConstants {
    /**
     * This property allow to specify how to generate column name when @Column.value is not specified:
     * com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder -- The field name is used and write using human-readable name. Example "first name"
     * com.deblock.cucumber.datatable.mapper.name.UseFieldNameColumnNameBuilder -- The field name is used without any transformation. Example "firstName"
     * com.deblock.cucumber.datatable.mapper.name.MultiNameColumnNameBuilder -- You can use human-readable name and fieldName.
     */
    public static final String NAME_BUILDER_CLASS_PROPERTY_NAME = "cucumber.datatable.mapper.name-builder-class";
    public static final String FIELD_RESOLVER_CLASS_PROPERTY_NAME = "cucumber.datatable.mapper.field-resolver-class";
}
