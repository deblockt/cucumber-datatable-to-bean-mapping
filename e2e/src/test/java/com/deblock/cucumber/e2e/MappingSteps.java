package com.deblock.cucumber.e2e;

import com.deblock.cucumber.e2e.beans.Account;
import com.deblock.cucumber.e2e.beans.CollectionValues;
import com.deblock.cucumber.e2e.beans.Conversation;
import com.deblock.cucumber.e2e.beans.CustomValues;
import com.deblock.cucumber.e2e.beans.Customer;
import com.deblock.cucumber.e2e.beans.DateValues;
import com.deblock.cucumber.e2e.beans.PrimitiveValues;
import com.deblock.cucumber.e2e.beans.Product;
import com.deblock.cucumber.e2e.beans.WrapperValues;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappingSteps {
    private Object mappedValue;

    @Given("a customer")
    public void aCustomer(Customer customer) {
        this.mappedValue = customer;
    }

    @Given("customers")
    public void customers(List<Customer> customers) {
        this.mappedValue = customers;
    }

    @Given("products")
    public void products(List<Product> products) {
        this.mappedValue = products;
    }

    @Given("primitive values")
    public void primitiveValues(List<PrimitiveValues> values) {
        this.mappedValue = values;
    }

    @Given("wrapper values")
    public void wrapperValues(List<WrapperValues> values) {
        this.mappedValue = values;
    }

    @Given("collection values")
    public void collectionValues(List<CollectionValues> values) {
        this.mappedValue = values;
    }

    @Given("date values")
    public void dateValues(List<DateValues> values) {
        this.mappedValue = values;
    }

    @Given("custom values")
    public void customValues(List<CustomValues> values) {
        this.mappedValue = values;
    }

    @Given("conversations")
    public void conversations(List<Conversation> conversations) {
        this.mappedValue = conversations;
    }

    @Given("accounts")
    public void accounts(List<Account> accounts) {
        this.mappedValue = accounts;
    }

    // The expected value is a docstring and not a datatable, so the check doesn't rely on the lib itself
    @Then("the mapped value is")
    public void theMappedValueIs(String expected) {
        assertEquals(expected.strip(), String.valueOf(this.mappedValue));
    }
}
