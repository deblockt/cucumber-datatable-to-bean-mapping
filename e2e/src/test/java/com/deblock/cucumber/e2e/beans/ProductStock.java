package com.deblock.cucumber.e2e.beans;

// Not a data table bean: only used as a JSON value of a Map column
public record ProductStock(String name, int stock) {
}
