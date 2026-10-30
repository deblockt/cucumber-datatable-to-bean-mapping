# Plan: end-to-end tests in CI

Status: in progress on `ci/e2e-tests` (2026-10-30). Steps 1-4 done (Cucumber 8.0.4 added), not committed

## Summary

The demos are the only end-to-end tests, but they can't run in CI:
- some scenarios fail on purpose to show the error messages, so `cucumberCli` always fails;
- most steps only `println` the mapped bean, so a wrong mapping still passes;
- a scenario can't check its own mapping error, because the mapping fails before the step runs.

Issue #34 (Cucumber's shaded Jackson) only showed up on some Cucumber versions. The e2e tests have to run against several of them.

## Design

### `e2e/` module

- A JUnit 5 test launches Cucumber through `cucumber-junit-platform-engine` with JUnit's `EngineTestKit`, and checks the result of each scenario. These are public APIs only, no Cucumber internals.
- `src/test/resources/e2e/mapping.feature`: scenarios that must pass. Each one maps a datatable, then checks the mapped value with `Then the mapped value is` and a docstring holding the expected `toString()`. The expected value is not a datatable, so the check doesn't rely on the lib itself.
- `src/test/resources/e2e/errors.feature`: scenarios that must fail. The JUnit test checks that each one fails and that its message contains the expected text.
- The glue package (`com.deblock.cucumber.e2e`) doesn't cover the lib's packages, so there are no `ClasspathScanner` warnings.
- Gradle only runs the Jupiter engine (`includeEngines 'junit-jupiter'`). The Cucumber engine is only launched by `EngineTestKit`.

### Version matrix: `e2e/versions.json`

Single source of truth for the Cucumber × JUnit combinations:

```json
[
  { "cucumber": "7.8.1",  "junit": "5.9.1" },
  { "cucumber": "7.10.0", "junit": "5.9.1" },
  { "cucumber": "7.34.2", "junit": "5.14.2" },
  { "cucumber": "8.0.4", "junit": "6.1.3" }
]
```

The JUnit version is the `junit-jupiter.version` declared in each `cucumber-junit-platform-engine` POM.

- `e2e/build.gradle` reads `-PcucumberVersion` and `-PjunitVersion`. Without them, it uses the last entry of `versions.json`, so `./gradlew build` still runs one combination.
- `e2e/run-all.sh` runs `:e2e:test` for every entry with `jq`, keeps going on failure, and prints an OK/KO summary.
- `.github/workflows/test.yml`: an `e2e-versions` job reads the file with `jq -c` and exposes it as an output, and an `e2e` job runs it with `matrix.include: fromJSON(...)` and `fail-fast: false`. The existing `build` job doesn't change.

## Steps

1. Prototype: `e2e/` module, `versions.json`, a few scenarios, run on the default version. Check that `EngineTestKit` works.
2. Run it on 7.8.1 and 7.10.0 (older JUnit 5.9.1).
3. `run-all.sh` and the GitHub workflow.
4. Cover the README features: records and classes, optional columns and default values, every Java type (primitives, lists, enums, dates, relative dates, `Map`), custom mappers (static and instance), nested beans, column name builders, error messages.
5. `cucumber-java8` variant: a second source set with lambda steps, or a `java8` flag in `versions.json`.
6. Move the intentional-failure scenarios from the demos to `errors.feature`, so the demos only hold passing scenarios and can run in CI too (replaces the "Demos" part of `ci-release.md`).
7. `CLAUDE.md`: how to run the e2e tests locally, and reproducing a CI failure with `-PcucumberVersion`.

## Decisions

1. **GitHub matrix rather than Gradle test suites** (2026-10-30): one check per version in the PR, versions run in parallel, simpler `build.gradle`.
2. **Matrix in `e2e/versions.json`** (2026-10-30), with the JUnit version in each entry, so the CI and local runs use the same list.
