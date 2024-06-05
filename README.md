# Cucumber datatable to bean mapping

The goal of this lib is to perform automatic mapping
between [gherkins datatable](https://cucumber.io/docs/gherkin/reference/#data-tables) and Java bean.
It provides some annotations to describe the mapping.

Two main features are provided:

- validate the datatable (validate that all headers are defined on bean)
- perform the mapping

## Installation

It's available on maven central repository.
You can find it
here [io.github.deblockt:cucumber-datatable-to-bean-mapping](https://mvnrepository.com/artifact/io.github.deblockt/cucumber-datatable-to-bean-mapping/latest)

This lib support only java 17.

## Sample

Using this bean:

``` java 
@DataTableWithHeader // this annotation is used to register this class as a Datatable
class Bean {
    @Column("column 1") // this annotation register this var as a column of the datatable
    public String column1;

    @Column(value = "column2", mandatory = false)
    public String column2;
}

// compatible with records
@DataTableWithHeader // this annotation is used to register this class as a Datatable
record Bean(
    @Column("column 1") // this annotation register this var as a column of the datatable
    String column1,
    @Column(value = "column2", mandatory = false)
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
  | column 1 | column 2 |
  | value1   | value2   |
  | value3   |          | 
```

And That's all. The mapping is performed automatically.

## Annotation usage

The following parameters are available on the `@Column` annotation

| name         | type     | description                                                                                                  |
|--------------|----------|--------------------------------------------------------------------------------------------------------------|
| value        | string[] | the column name. The default value is the field name. You can specify multiple name for the same column      |
| description  | string   | the column description. The description is displayed when a datatable is malformed to show a helper message. |
| mandatory    | boolean  | default true. Can be set to false to set the column to optional                                              |
| defaultValue | string   | the default value used if the column is not specified                                                        |

## Java type support

The following types can be auto mapped: 
  - `int`, `long`, `short`, `float`, `double`, `BigDecimal`, `BigInteger`: decimal separator can be `.` or `,`. Example `10.05`
  - `boolean`: true/false
  - `string`: trim the string from the datatable
  - `enum`: the enum name can be used on datatable
  - `List`, `Set`, `Collection`: the generic can be a type managed by the mapping. Item on datatable are split using a `,`
  - `OffsetDateTime`, `LocalDateTime`, `LocalDate`: date time. Can be ISO formatted date or relative date. 
     
     Relative date can be specified using `now` keyword.  
     
     You can add or subtract amount of time from now. `now + 1 day`, `now - 3 weeks`.
    
     `now` will return the same date at each step of the test.

### Custom type support

If you want to use a custom type on datatable, you can write custom mapper. It can be useful when you want to convert id to object. 

For example, If you have an object `Customer`:
```java
@DataTableWithHeader
record Customer(
        @Column("code")
        String code,
        @Column("first name")
        String firstName,
        @Column("last name")
        String lastName
) {}
```
And you want map a datatable to the following bean: 
``` java
@DataTableWithHeader
record Bean(
    @Column("customer code")
    Customer customer
) { }
```
If you want this steps to work
```gherkin
Given the following customer exists
  | code     | first name | last name |
  | deblockt | Thomas     | Deblock   |
When I want to do something with a customer
  | customer code |
  | deblockt      |
```

You need to write a function to map a string to a Customer. 
The function should be provided on your steps package.

```java
@CustomDatatableFieldMapper(sample = "cucumberCode", typeDescription = "Customer")
public static Customer customerMapper(String customerCode) {
    return TestContext.getCustomer(customerCode);
}
```

## Nested Datatable object support

If you have big datatable, you can organize your objects using nested object. 
For example, if you have a `Customer` object, you can have a `PersonalInformation` Object. 
```java
@DataTableWithHeader
record Customer(
    @Column
    String id,
    @Column
    PersonalInformation personalInformation
) {}

@DataTableWithHeader
record PersonalInformation(
    @Column
    String firstName,
    @Column
    String lastName
) {}
```

Using these objects, the datatable will look like 
```gherkin
| id | first name | last name |
| 10 | Thomas     | Deblock   |
```

Now If you another object `Conversation` with two customer, like that: 
```java
@DataTableWithHeader
record Conversation(
    @Column 
    Customer customer1,
    @Column
    Customer customer2
) {}
```

If you write a datatable, you can not know if `first name` column is the first name of the `customer1` or the `customer2`.
To fix this issue, you can override the fields name using `<parent_name>`, see this example: 
```java
@DataTableWithHeader
record Conversation(
    @Column(value = "customer1", mandatory = false)
    Customer customer1,
    @Column(value = "customer2", mandatory = false)
    Customer customer2
) {}

// parent_name will be replaced by customer1 or customer2
@DataTableWithHeader
record Customer(
        @Column("<parent_name> id") 
        String id,
        @Column("<parent_name>")
        PersonalInformation personalInformation
) {}

// parent_name will be replaced by customer1 or customer2 coming from personalInformation annotation
@DataTableWithHeader
record PersonalInformation(
        @Column("<parent_name> first name") 
        String firstName,
        @Column("<parent_name> last name")
        String lastName
) {}
```

Using these objects, the following datatable will works:
```gherkin
# generate a Conversation object with only a customer1, and null for customer2
| customer1 id | customer1 first name | customer1 last name |
| 10           | Thomas               | Deblock             |

# generate a Conversation object with only a customer2, and null for customer1
| customer2 id | customer2 first name | customer2 last name |
| 10           | Thomas               | Deblock             |

# generate a Conversation object with a customer2, and a customer1
| customer2 id | customer2 first name | customer2 last name | customer2 id | customer2 first name | customer2 last name |
| 10           | Thomas               | Deblock             | 11           | Nicolas              | Deblock             |
```

## Usage on a fat/uber jar

If you need to build an executable fat jar to run your cucumber, you should add some configuration to work with this library.

This library use the [java SPI definition](https://www.baeldung.com/java-spi) to inject some class on cucumber runtime.

When you build a fat jar, this configuration can be override by cucumber definition, so we need to merge configuration files.

### Gradle 

Using gradle you can use the [shadow plugin](https://imperceptiblethoughts.com/shadow/introduction/) to solve this issue.
You can read [this chapter](https://imperceptiblethoughts.com/shadow/configuration/merging/#merging-service-descriptor-files) to see how to merge configuration files.

example of task configuration:
``` gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '7.1.2'
}

shadowJar {
    // specify jar name informations
    archiveBaseName.set('cucumber-tests')
    archiveClassifier.set('')

    // add main and tests sources on jar
    from sourceSets.main.output
    from sourceSets.test.output
    configurations = [
        project.configurations.compileClasspath,
        project.configurations.cucumberRuntime
    ]
    
    // specify the cucumber Main class
    manifest {
        attributes "Main-Class": "io.cucumber.core.cli.Main"
    }

    // merge service files. Need to work with this lib.
    mergeServiceFiles()
}
```

### Maven

Using maven you can use the [maven-shade-plugin](https://maven.apache.org/plugins/maven-shade-plugin/index.html) to solve this issue. 
You can use this plugin configuration: 
``` xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.4</version>
    <executions>
        <execution>
            <goals>
                <goal>shade</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
