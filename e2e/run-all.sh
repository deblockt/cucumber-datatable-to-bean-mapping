#!/usr/bin/env bash
# Runs the e2e tests for every Cucumber x JUnit combination of versions.json, like the CI matrix.
set -uo pipefail
cd "$(dirname "$0")/.."

failed=()
while read -r cucumber junit; do
  echo "=== Cucumber $cucumber / JUnit $junit"
  ./gradlew :e2e:test -PcucumberVersion="$cucumber" -PjunitVersion="$junit" \
    || failed+=("cucumber $cucumber")
done < <(jq -r '.[] | "\(.cucumber) \(.junit)"' e2e/versions.json)

if [ ${#failed[@]} -eq 0 ]; then
  echo "All combinations passed"
else
  echo "Failed: ${failed[*]}"
  exit 1
fi
