package com.deblock.cucumber.e2e;

import com.deblock.cucumber.datatable.annotations.CustomDatatableFieldMapper;
import com.deblock.cucumber.e2e.beans.Code;
import com.deblock.cucumber.e2e.beans.Reference;
import io.cucumber.java.en.Given;

public class CustomMapperSteps {
    private String referencePrefix = "none";

    @CustomDatatableFieldMapper(sample = "ABC", typeDescription = "Code")
    public static Code codeMapper(String value) {
        if (!value.matches("[A-Z]+")) {
            throw new IllegalArgumentException("a code only contains upper case letters");
        }
        return new Code(value);
    }

    // Not static: called on the instance created by Cucumber for the scenario
    @CustomDatatableFieldMapper(sample = "R1", typeDescription = "Reference")
    public Reference referenceMapper(String value) {
        return new Reference(this.referencePrefix, value);
    }

    @Given("the reference prefix is {string}")
    public void theReferencePrefixIs(String prefix) {
        this.referencePrefix = prefix;
    }
}
