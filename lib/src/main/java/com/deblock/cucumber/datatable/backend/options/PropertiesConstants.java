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
    /**
     * This property allow to specify how to get the now date time.
     * com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService -- The now date is static and be the same during all the test execution
     */
    public static final String DATE_TIME_SERVICE_CLASS_PROPERTY_NAME = "cucumber.datatable.mapper.date-time-service-class";
}
