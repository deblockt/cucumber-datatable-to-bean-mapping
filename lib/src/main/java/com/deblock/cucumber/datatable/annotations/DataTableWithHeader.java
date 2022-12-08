package com.deblock.cucumber.datatable.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used on a java class or record to register is as a datatable compliant bean.
 *
 * <pre>
 *   \@DataTableWithHeader
 *   class Bean {
 *       \@Column("column 1")
 *       public String column1;
 *
 *       \@Column(value = "column2", mandatory = false)
 *       public String column2;
 *   }
 *   \@DataTableWithHeader
 *   record Bean(
 *       \@Column("column 1")
 *       String column1,
 *       \@Column(value = "column2", mandatory = false)
 *       String column2
 *   ) { }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.RECORD_COMPONENT})
public @interface DataTableWithHeader {
}
