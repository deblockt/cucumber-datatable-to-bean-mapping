# Cucumber datatable to bean mapping

[![Maven Central](https://img.shields.io/maven-central/v/io.github.deblockt/cucumber-datatable-to-bean-mapping)](https://central.sonatype.com/artifact/io.github.deblockt/cucumber-datatable-to-bean-mapping)

The goal of this library is to map
[Gherkin data tables](https://cucumber.io/docs/gherkin/reference/#data-tables) to Java beans automatically.
It provides annotations to describe the mapping.

It provides two main features:

- validating the data table (checking that all headers are defined on the bean), see [validation](#validation)
- performing the mapping

## Installation

The library is available on Maven Central:
[io.github.deblockt:cucumber-datatable-to-bean-mapping](https://central.sonatype.com/artifact/io.github.deblockt/cucumber-datatable-to-bean-mapping).
Replace `<version>` with the version shown on the badge above.

Gradle:

``` gradle
testImplementation 'io.github.deblockt:cucumber-datatable-to-bean-mapping:<version>'
```

Maven:

``` xml
<dependency>
    <groupId>io.github.deblockt</groupId>
    <artifactId>cucumber-datatable-to-bean-mapping</artifactId>
    <version><version></version>
    <scope>test</scope>
</dependency>
```

This library does not bring Cucumber with it: add your own Cucumber dependency (for example `io.cucumber:cucumber-java`).
It is built and tested with Cucumber 7.

This library supports only Java 17+.

## Sample

Using this bean:

``` java 
@DataTableWithHeader // registers this class as a data table
class Bean {
    @Column("column 1") // registers this field as a column of the data table
    public String column1;

    @Column(mandatory = false) // the column is optional. The column name is built from the field name
    public String column2;
}
```

Records are supported too:

``` java
@DataTableWithHeader // registers this record as a data table
record Bean(
    @Column("column 1") // registers this component as a column of the data table
    String column1,
    @Column(mandatory = false) // the column is optional. The column name is built from the field name
    String column2
) { }

```

And this step definition:

``` java
// java style
@Given("a step with a datatable")
public void stepWithList(List<Bean> beans) {
    System.out.println("read: " + beans);
}

// java-8 style
Given(
    "a step with a datatable", 
    (DataTable datatable) -> System.out.println("read: " + datatable.asList(Bean.class))
);

```

You can write this step:

``` gherkin
Then a step with a datatable
  | column 1 | column2 |
  | value1   | value2  |
  | value3   |         |
```

And that's all: the mapping is performed automatically.

## Validation

Before the mapping, the headers of the data table are checked against the bean.
If a mandatory column is missing, or if a column is not defined on the bean, the step fails with a message
describing the expected data table.

For example, with this bean:

``` java
@DataTableWithHeader
record Customer(
    @Column(value = "first name", description = "the customer first name")
    String firstName,
    @Column(value = "last name", description = "the customer last name")
    String lastName,
    @Column(value = "vip", mandatory = false, defaultValue = "false")
    boolean vip
) { }
```

This data table:

``` gherkin
Given the following customers
  | first name |
  | Thomas     |
```

fails with:

```
The following headers are mandatory : "last name"
| first name | last name | vip  |
| string     | string    | true |

first name (mandatory, type: string): the customer first name
last name (mandatory, type: string): the customer last name
vip (optional, default: false, type: boolean)
```

## Annotation usage

The following parameters are available on the `@Column` annotation. The `@Column` annotation can be omitted, see the [field resolution chapter](#field-resolution).

| name         | type     | description                                                                                                  |
|--------------|----------|--------------------------------------------------------------------------------------------------------------|
| value        | string[] | the column name. You can specify several names for the same column. The default value is built from the field name, see the [name resolution chapter](#name-resolution) |
| description  | string   | the column description. It is displayed as a help message when a data table is malformed.                    |
| mandatory    | boolean  | default true. Set it to false to make the column optional                                                    |
| defaultValue | string   | the default value used if the column is not specified                                                        |

## Java type support

The following types can be auto mapped. Wrapper types (`Integer`, `Long`, `Boolean`...) are supported too.

- `int`, `long`, `short`, `float`, `double`, `BigDecimal`, `BigInteger`: the decimal separator can be `.` or `,`.
  Example `10.05` or `10,05`.
  Thousands separators are not supported: `,` is always read as the decimal separator, so `1,000` is read as `1.0` for a `double` or a `BigDecimal`, and is an invalid value for an `int`. Write `1000` instead.
- `boolean`: `true` or `false`
- `String`: the value from the data table is trimmed
- `UUID`: a standard UUID string. Example `123e4567-e89b-12d3-a456-426614174000`
- `enum`: use the enum constant name in the data table
- `List`, `Set`, `Collection`: the generic type can be any type supported by the mapping. Items are split using a `,` and trimmed.
  Example `1, 3, 5` for a `List<Integer>`.
  As `,` is the item separator, use `.` as the decimal separator in a list: `1.5, 2.5` for a `List<Double>`.
  `1,5` is read as two items, `1` and `5`.
- `Map`: the cell is a JSON object, parsed with [Gson](https://github.com/google/gson) to the generic types of the map.
  Example `{"math": [12, 15], "music": []}` for a `Map<String, List<Integer>>`
- `OffsetDateTime`, `LocalDateTime`, `LocalDate`: an ISO formatted date or a relative date.

  A relative date uses the `now` keyword.

  You can add or subtract an amount of time from now: `now + 1 day`, `now - 3 weeks`.
  The available units are `day`, `week`, `month` and `year`, plus `hour`, `minute`, `second` and `milli` for
  `OffsetDateTime` and `LocalDateTime`. Each unit can be written in singular or plural form.

  `now` returns the same date at each step of the test.

> :information_source: You can customize how `now` is resolved.
>
> Implement the interface `com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService` with your own `now` function.
>
> To use your class, set the property `cucumber.datatable.mapper.date-time-service-class` to its fully qualified name.
>
> You also need to create a file `META-INF/services/com.deblock.cucumber.datatable.mapper.typemetadata.date.DateTimeService` containing your class name.

### Custom type support

If you want to use a custom type in a data table, you can write a custom mapper.
It can be useful when you want to convert an id to an object.

For example, if you have an object `Customer`:

```java
record Customer(String code, String firstName, String lastName) {
}
```

And you want to map a data table to the following bean:

``` java
@DataTableWithHeader
record Bean(
    @Column("customer code")
    Customer customer
) { }
```

If you want these steps to work:

```gherkin
Given the following customer exists
| code     | first name | last name |
| deblockt | Thomas     | Deblock   |
When I want to do something with a customer
| customer code |
| deblockt      |
```

You need to write a function that maps a string to a `Customer`.
The function must be declared in your step definitions package.

```java
@CustomDatatableFieldMapper(sample = "cucumberCode", typeDescription = "Customer")
public Customer customerMapper(String customerCode) {
    // Get the customer from your test context (a static context, or one injected with Spring or PicoContainer)
}
```

## Nested object support

If you have big data tables, you can organize your objects using nested objects.
For example, a `Customer` object can contain a `PersonalInformation` object.

```java

@DataTableWithHeader
record Customer(
        @Column
        String id,
        @Column
        PersonalInformation personalInformation
) {
}

@DataTableWithHeader
record PersonalInformation(
        @Column
        String firstName,
        @Column
        String lastName
) {
}
```

Using these objects, the data table looks like this:

```gherkin
| id | first name | last name |
| 10 | Thomas     | Deblock   |
```

Now, if you have another object `Conversation` with two customers, like this:

```java

@DataTableWithHeader
record Conversation(
        @Column
        Customer customer1,
        @Column
        Customer customer2
) {
}
```

In a data table, you cannot tell whether the `first name` column is the first name of `customer1` or
`customer2`.
To fix this issue, you can prefix the column names using `<parent_name>`, see this example:

```java

@DataTableWithHeader
record Conversation(
        @Column(value = "customer1", mandatory = false)
        Customer customer1,
        @Column(value = "customer2", mandatory = false)
        Customer customer2
) {
}

// parent_name will be replaced by customer1 or customer2
@DataTableWithHeader
record Customer(
        @Column("<parent_name> id")
        String id,
        @Column("<parent_name>")
        PersonalInformation personalInformation
) {
}

// parent_name will be replaced by customer1 or customer2, coming from the personalInformation annotation
@DataTableWithHeader
record PersonalInformation(
        @Column("<parent_name> first name")
        String firstName,
        @Column("<parent_name> last name")
        String lastName
) {
}
```

Using these objects, the following data tables work:

```gherkin
# generate a Conversation object with only a customer1, and null for customer2
| customer1 id | customer1 first name | customer1 last name |
| 10           | Thomas               | Deblock             |

# generate a Conversation object with only a customer2, and null for customer1
| customer2 id | customer2 first name | customer2 last name |
| 10           | Thomas               | Deblock             |

# generate a Conversation object with a customer1 and a customer2
| customer1 id | customer1 first name | customer1 last name | customer2 id | customer2 first name | customer2 last name |
| 10           | Thomas               | Deblock             | 11           | Nicolas              | Deblock             |
```

## Name resolution

When the column name is not explicitly specified using the `@Column` annotation, the name is derived from the field name. Three naming strategies are available:

1. **Human Readable _(default)_**: Converts camel case field names to human-readable names, separating words with spaces.
   - Example: `myFieldName` becomes `my field name`.
2. **Field Name**: Keeps the original field name as the column name.
   - Example: `myFieldName` becomes `myFieldName`.
3. **Multi Name**: Combines the previous two strategies, allowing either the original field name or the human-readable name.
   - Example: `myFieldName` becomes `myFieldName` or `my field name`.

See the [configuration](#configuration) section to learn how to select a naming strategy.


## Field resolution

By default, only fields with the `@Column` annotation are used as data table columns.
If you don't want to add `@Column` to all your fields, you can set the property `cucumber.datatable.mapper.field-resolver-class` to `com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.ImplicitFieldResolver`. In this case, all the fields of your class are mapped as optional columns.

With this configuration, this class is valid:
``` java
@DataTableWithHeader
class Bean {
    public String firstColumn;
    public String secondColumn;
}
```
If you want to ignore a field, use the `@Ignore` annotation.
``` java
@DataTableWithHeader
class Bean {
    public String firstColumn;
    public String secondColumn;
    @Ignore
    public ComplexType fieldToIgnore;
}
```

> :warning: Only the fields declared on the class itself are mapped. Fields inherited from a parent class are ignored, even if they have a `@Column` annotation.

## Configuration

You can configure some features of this library to suit your coding preferences.
The configuration works the same way as the [Cucumber configuration](https://cucumber.io/docs/cucumber/api/?lang=java#options).

The following properties can be set in `cucumber.properties`, or using environment variables or system properties.
```properties
# The property cucumber.datatable.mapper.name-builder-class accepts these values:
# com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder -- The field name is written as a human-readable name. Example "first name"
# com.deblock.cucumber.datatable.mapper.name.UseFieldNameColumnNameBuilder -- The field name is used without any transformation. Example "firstName"
# com.deblock.cucumber.datatable.mapper.name.MultiNameColumnNameBuilder -- Both the human-readable name and the field name can be used.
cucumber.datatable.mapper.name-builder-class=com.deblock.cucumber.datatable.mapper.name.HumanReadableColumnNameBuilder
# The property cucumber.datatable.mapper.field-resolver-class accepts these values:
# com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver -- Only fields with the @Column annotation are mapped
# com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.ImplicitFieldResolver -- All public fields, or fields with a setter, are mapped. Use @Ignore to skip a field
cucumber.datatable.mapper.field-resolver-class=com.deblock.cucumber.datatable.mapper.datatable.fieldresolvers.DeclarativeFieldResolver
# The property cucumber.datatable.mapper.date-time-service-class sets how the `now` date time is computed. You can implement your own class to customize it
# com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService -- The now date is static and stays the same during the whole test execution
cucumber.datatable.mapper.date-time-service-class=com.deblock.cucumber.datatable.mapper.typemetadata.date.StaticDateTimeService
```

## Usage on a fat/uber jar

If you build an executable fat jar to run your Cucumber tests, you need some extra configuration to make this
library work.

This library uses [Java SPI](https://www.baeldung.com/java-spi) to register its classes in the Cucumber runtime.

When you build a fat jar, the `META-INF/services` files of this library can be overwritten by the Cucumber ones, so they
need to be merged.

### Gradle

With Gradle, you can use the [Shadow plugin](https://gradleup.com/shadow/).
Read [this chapter](https://gradleup.com/shadow/configuration/merging/) to learn how to merge service files.

Example of task configuration:

``` gradle
plugins {
    id 'com.gradleup.shadow' version '8.3.6'
}

shadowJar {
    // jar name
    archiveBaseName.set('cucumber-tests')
    archiveClassifier.set('')

    // add main and test sources to the jar
    from sourceSets.main.output
    from sourceSets.test.output
    configurations = [
        project.configurations.compileClasspath,
        project.configurations.cucumberRuntime
    ]

    // the Cucumber main class
    manifest {
        attributes "Main-Class": "io.cucumber.core.cli.Main"
    }

    // merge service files. Required by this library.
    mergeServiceFiles()
}
```

### Maven

With Maven, you can use the [maven-shade-plugin](https://maven.apache.org/plugins/maven-shade-plugin/index.html).
The `ServicesResourceTransformer` merges the service files:

``` xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.6.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <transformers>
                    <!-- merge service files. Required by this library. -->
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                    <!-- the Cucumber main class -->
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>io.cucumber.core.cli.Main</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## IDE plugins

### IntelliJ

An IntelliJ plugin is available for this library.
You can install it from the [JetBrains Marketplace: Cucumber DataTable Mapping](https://plugins.jetbrains.com/plugin/24873-cucumber-datatable-mapping)
