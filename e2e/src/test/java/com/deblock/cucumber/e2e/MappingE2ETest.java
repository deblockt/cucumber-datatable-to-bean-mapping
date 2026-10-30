package com.deblock.cucumber.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Execution;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathResource;

/**
 * Runs the e2e features with the Cucumber engine, and checks the result of each scenario.
 */
class MappingE2ETest {

    @ParameterizedTest
    @ValueSource(strings = {
            "e2e/mapping.feature",
            "e2e/types.feature",
            "e2e/collections.feature",
            "e2e/dates.feature",
            "e2e/custom-mappers.feature",
            "e2e/nested.feature"
    })
    void allScenariosPass(String feature) {
        final List<Execution> scenarios = runFeature(feature);

        assertFalse(scenarios.isEmpty(), "no scenario was run in " + feature);
        final String failures = scenarios.stream()
                .filter(scenario -> scenario.getTerminationInfo().getExecutionResult().getStatus() != TestExecutionResult.Status.SUCCESSFUL)
                .map(scenario -> "- " + scenarioName(scenario) + ": " + failureMessage(scenario))
                .collect(Collectors.joining("\n"));
        assertTrue(failures.isEmpty(), "failed scenarios in " + feature + ":\n" + failures);
    }

    @Test
    void errorScenariosFailWithAClearMessage() {
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

        assertEquals(expectedMessages.keySet(), actualMessages.keySet(), "scenarios of errors.feature");
        expectedMessages.forEach((scenario, expectedMessage) -> {
            final String actualMessage = actualMessages.get(scenario);
            if (actualMessage == null || !actualMessage.contains(expectedMessage)) {
                fail("scenario \"" + scenario + "\" should fail with a message containing:\n" + expectedMessage
                        + "\nbut was:\n" + actualMessage);
            }
        });
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
