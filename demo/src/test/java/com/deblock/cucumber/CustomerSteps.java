package com.deblock.cucumber;

import com.deblock.cucumber.beans.CucumberEntry;
import io.cucumber.java.en.Given;

public class CustomerSteps {

    @Given("I create a customer using my API")
    public void step(CucumberEntry cucumberEntryList) {
        System.out.println("creating customer: " + cucumberEntryList);
    }

}
