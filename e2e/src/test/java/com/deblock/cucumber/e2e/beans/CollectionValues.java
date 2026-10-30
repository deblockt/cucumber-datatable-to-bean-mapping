package com.deblock.cucumber.e2e.beans;

import com.deblock.cucumber.datatable.annotations.Column;
import com.deblock.cucumber.datatable.annotations.DataTableWithHeader;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DataTableWithHeader
public record CollectionValues(
    @Column(value = "integer list", mandatory = false) List<Integer> integerList,
    @Column(value = "double list", mandatory = false) List<Double> doubleList,
    @Column(value = "level list", mandatory = false) List<Level> levelList,
    @Column(value = "integer set", mandatory = false) Set<Integer> integerSet,
    @Column(value = "string collection", mandatory = false) Collection<String> stringCollection,
    @Column(value = "scores", mandatory = false) Map<String, List<Integer>> scores,
    @Column(value = "products", mandatory = false) Map<String, ProductStock> products
) {
}
