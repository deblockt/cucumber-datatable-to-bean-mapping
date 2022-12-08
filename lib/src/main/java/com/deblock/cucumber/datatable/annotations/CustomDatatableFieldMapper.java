package com.deblock.cucumber.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This function allow to define a customer mapper.
 * The annotated function should:
 *   - be static
 *   - have one string parameter
 *   - be defined on glue package
 *
 * <pre>
 *   @CustomDatatableFieldMapper(sample = "cucumberCode", typeDescription = "Customer")
 *   public static Customer customerMapper(String customerCode) {
 *     return TestContext.getCustomer(customerCode);
 *   }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CustomDatatableFieldMapper {
    /**
     * a sample value use to display hint when datatable is malformed
     */
    String sample();

    /**
     * a brief description of the type. This value is used to display hint when datatable is malformed
     */
    String typeDescription();
}
