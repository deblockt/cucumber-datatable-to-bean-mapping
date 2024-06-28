package com.deblock.cucumber.datatable.backend.options;

public final class PropertiesConstants {
    /**
     * This property allow to specify how to generate column name when @Column.value is not specified:
     * com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder -- The field name is used and write using human-readable name. Example "first name"
     * com.deblock.cucumber.datatable.mapper.name.UseFieldNameColumnNameBuilder -- The field name is used without any transformation. Example "firstName"
     * com.deblock.cucumber.datatable.mapper.name.MultiNameColumnNameBuilder -- You can use human-readable name and fieldName.
     */
    public static final String NAME_BUILDER_CLASS_PROPERTY_NAME = "cucumber.datatable.mapper.name-builder-class";
    /**
     * This property allow to specify how to generate dataTable field list from your class.
     * com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver -- Register only fields with @Column annotation
     * com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.ImplicitFieldResolver -- Register all fields public or with a setter. @Ignore annotation can be used to ignore a field
     */
    public static final String FIELD_RESOLVER_CLASS_PROPERTY_NAME = "cucumber.datatable.mapper.field-resolver-class";
}
