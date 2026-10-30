# Plan: issue #34, NoClassDefFoundError on Cucumber's shaded Jackson

Status: implemented with Gson on `fix/34-cucumber-internal-jackson` (commit 2fe3eb9, 2026-10-24), PR #35

## Summary

`MapTypeMetadata` uses Jackson classes that Cucumber shades for its own internal use. Those packages aren't public API, and their path changes between Cucumber versions. Release 1.2.0 switched to the `io.cucumber.messages.ndjson.internal...` path, so map conversion breaks on any Cucumber version that doesn't ship `messages-ndjson`. That includes the 8.x line from the report and older 7.x versions like 7.8.1 and 7.10.0.

**Reproduced** on `main`: `./gradlew :demo-java8:cucumberCli` (Cucumber 7.10.0, `RecordBean.aMap`) fails with
`NoClassDefFoundError: io/cucumber/messages/ndjson/internal/com/fasterxml/jackson/core/type/TypeReference`.
`demo/` (no `Map` field) is not affected: the `NoClassDefFoundError`s it prints are only warnings from Cucumber's `ClasspathScanner` (its glue `com.deblock.cucumber` also covers the lib's package). Its 3 failing scenarios are expected ones (two intentional, plus a `customDTO` header mismatch that already fails since 1.1.0).

### Lib version × Cucumber version matrix

Each tag's own demos, run with their original Cucumber version and with 7.34.2. "Crash" = `CompositeCucumberException` during glue loading, 0 scenarios run.

| Lib   | Shaded package used            | demo-java8 on 7.10.0 | demo-java8 on 7.34.2 |
|-------|--------------------------------|----------------------|----------------------|
| 1.0.3 | `io.cucumber.core.internal`    | OK                   | crash                |
| 1.1.0 | `io.cucumber.core.internal`    | OK                   | crash                |
| 1.1.3 | `io.cucumber.core.internal`    | OK                   | crash                |
| 1.2.0 | `io.cucumber.messages.ndjson.internal` | crash        | OK                   |

`demo/` never crashes (only expected failures) in any combination. Every released version only works with part of the Cucumber range, which confirms the root cause: the fix is to stop depending on Cucumber's shaded Jackson at all.

## Root cause

- `lib/src/main/java/com/deblock/cucumber/datatable/mapper/typemetadata/map/MapTypeMetadata.java:4-6` imports `JsonProcessingException`, `TypeReference` and `ObjectMapper` from Cucumber's shaded package. Commit d29b3ef (#33) moved these imports from `io.cucumber.core.internal...` to `io.cucumber.messages.ndjson.internal...` while upgrading `cucumber-core` to 7.34.2.
- Cucumber 7.8.1 and 7.10.0 only contain `io.cucumber.core.internal...`, and 7.34.2 only contains the `messages-ndjson` path (checked in the jars). Any version of the library can therefore only work with part of the Cucumber range.
- The lib unit tests didn't catch it because they run on 7.34.2. The demos would have caught it, but `./gradlew build` never runs their `.feature` files.

## Changes

- `lib/build.gradle`: add `implementation 'com.google.code.gson:gson:2.14.0'` (~310 KB, only transitive dependency is `error_prone_annotations`; 2.15.0 was published on 2026-10-24, too fresh). `cucumber-core` stays `compileOnly`.
- `MapTypeMetadata.java`: replace the shaded Jackson `ObjectMapper` with `new Gson().fromJson(value, parameterizedType)`, and map `JsonParseException` to `ConversionError`. Gson reads a `java.lang.reflect.Type` directly, so generic values and nested POJOs keep working.
- `MapTypeMetadataFactoryTest` and `MapTypeMetadataTest`: replace the shaded `TypeReference` with Gson's `TypeToken`. Adapt the malformed-JSON test to Gson's error message.

## Tests

- New guard unit test in `lib/src/test` that fails today. It reads the compiled bytecode of the lib's main classes and asserts that none of them reference a `io/cucumber/**/internal/**` class, so this can't come back with a future Cucumber upgrade.
- `demo-java8` already has a `Map` scenario (`RecordBean.aMap`) on Cucumber 7.10.0, and it's the end-to-end regression test. It fails today and has to pass after the fix. `demo/` runs on 7.8.1 and gets checked too.

## Docs

- README: document `Map` support in "Java type support" (it wasn't documented): the cell is a JSON object parsed with Gson. No configuration changes.

## Decisions

1. **Gson rather than Jackson** (decided 2026-10-24): ~8× lighter than `jackson-databind` + `jackson-core` + annotations (~2.2 MB), no meaningful transitive dependency, same behavior for `Map` conversion. A home-made parser (zero dependency, keys/values converted with the lib's `TypeMetadata`) was considered and left for a possible separate `feat:`.
2. **"Bump the baseline to Cucumber 8.x" is out of scope.** The fix makes the lib independent of Cucumber's internals, so the compile baseline doesn't need to change. A Cucumber version bump would be a separate `chore:`. Cucumber 8.x hasn't been tested locally.

## Branch and commit

- Branch: `fix/34-cucumber-internal-jackson`
- Commit: `fix: use Gson instead of Cucumber's shaded Jackson (#34)` with `Fixes #34` in the body
