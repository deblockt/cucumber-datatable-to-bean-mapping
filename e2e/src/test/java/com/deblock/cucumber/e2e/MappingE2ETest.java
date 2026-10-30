package com.deblock.cucumber.e2e;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Execution;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathResource;

/**
 * Runs the e2e features with the Cucumber engine, and checks the result of each scenario.
 * Each scenario is reported as its own test, so a failure gives the feature and the scenario name.
 */
class MappingE2ETest {

    @TestFactory
    Stream<DynamicNode> allScenariosPass() {
        return Stream.of(
                "e2e/mapping.feature",
                "e2e/types.feature",
                "e2e/collections.feature",
                "e2e/dates.feature",
                "e2e/custom-mappers.feature",
                "e2e/nested.feature"
        ).map(feature -> {
            final List<Execution> scenarios = runFeature(feature);
            if (scenarios.isEmpty()) {
                return dynamicTest(feature, () -> fail("no scenario was run in " + feature));
            }
            return dynamicContainer(feature, scenarios.stream()
                    .map(scenario -> dynamicTest(scenarioName(scenario), () -> rethrowFailure(scenario))));
        });
    }

    @TestFactory
    Stream<DynamicNode> errorScenariosFailWithAClearMessage() {
        final Map<String, String> expectedMessages = new LinkedHashMap<>();
        expectedMessages.put("Unknown column", "The following headers \"last nam\" are not defined for this dataTable");
        expectedMessages.put("Missing mandatory column", "The following headers are mandatory : \"last name\"");
        expectedMessages.put("Invalid number", "Unable to convert value \"twelve\" for column \"integer\"");
        expectedMessages.put("Unknown enum value", "Unable to convert value \"BRONZE\" for column \"level\"");
        expectedMessages.put("Invalid UUID", "Unable to convert value \"not-a-uuid\" for column \"uuid\"");
        expectedMessages.put("Invalid date", "Unable to convert value \"tomorrow\" for column \"local date\"");
        expectedMessages.put("Invalid JSON for a map column", "for column \"scores\"");
        expectedMessages.put("Custom mapper throws an exception",
                "Unable to convert value \"abc\" for column \"code\" of type Code: a code only contains upper case letters");

        final Map<String, String> actualMessages = runFeature("e2e/errors.feature").stream()
                .collect(Collectors.toMap(MappingE2ETest::scenarioName, MappingE2ETest::failureMessage, (a, b) -> a, LinkedHashMap::new));

        final Stream<DynamicNode> sameScenarios = Stream.of(dynamicTest("every scenario has an expected message",
                () -> assertEquals(expectedMessages.keySet(), actualMessages.keySet(), "scenarios of errors.feature")));
        final Stream<DynamicNode> messages = expectedMessages.entrySet().stream()
                .map(expected -> dynamicTest(expected.getKey(), () -> {
                    final String actualMessage = actualMessages.get(expected.getKey());
                    if (actualMessage == null || !actualMessage.contains(expected.getValue())) {
                        fail("scenario \"" + expected.getKey() + "\" should fail with a message containing:\n" + expected.getValue()
                                + "\nbut was:\n" + actualMessage);
                    }
                }));
        return Stream.concat(sameScenarios, messages);
    }

    private static List<Execution> runFeature(String feature) {
        final EngineExecutionResults results = EngineTestKit.engine("cucumber")
                .selectors(selectClasspathResource(feature))
                .configurationParameter("cucumber.glue", "com.deblock.cucumber.e2e")
                .configurationParameter("cucumber.publish.quiet", "true")
                .execute();

        // When Cucumber fails before running the scenarios (glue loading, backend), only a container fails
        results.containerEvents().executions().failed().stream().findFirst().ifPresent(execution -> {
            throw new AssertionError(
                    "Cucumber failed before running the scenarios of " + feature,
                    execution.getTerminationInfo().getExecutionResult().getThrowable().orElse(null)
            );
        });
        return results.testEvents().executions().list();
    }

    // Rethrows the scenario failure as is, so the IDE and Gradle show its expected / actual values
    private static void rethrowFailure(Execution scenario) throws Throwable {
        final TestExecutionResult result = scenario.getTerminationInfo().getExecutionResult();
        if (result.getStatus() != TestExecutionResult.Status.SUCCESSFUL) {
            throw result.getThrowable().orElseGet(() -> new AssertionError("scenario " + result.getStatus()));
        }
    }

    private static String scenarioName(Execution scenario) {
        return scenario.getTestDescriptor().getDisplayName();
    }

    // null when the scenario passed
    private static String failureMessage(Execution scenario) {
        final TestExecutionResult result = scenario.getTerminationInfo().getExecutionResult();
        if (result.getStatus() == TestExecutionResult.Status.SUCCESSFUL) {
            return null;
        }
        return result.getThrowable().map(String::valueOf).orElse(result.getStatus().name());
    }
}
