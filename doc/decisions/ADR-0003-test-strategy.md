---
id: ADR-0003
title: Test strategy
status: proposed
date: 2026-10-10
---

# ADR-0003: Test strategy

## Context

The open question was what the verification of the project consists of and which test programs exist.
The choice to be decided was how test programs are named, stored, and run.

## Decision

1. The test programs of the project are located under `src/test/java`.
2. Test programs run with JUnit 5; the version is bound by the imported `bom-core`, and the assertions come
   from `org.junit.jupiter.api.Assertions`. No further test framework is introduced.
3. A test program is a class under `src/test/java` that carries at least one `@Test` method and whose name
   follows the selection pattern of the test run (the names `Test*` and `*Test`). A base or helper class
   without a `@Test` method is not a test program, even if its name matches the pattern.
4. A test program runs without an application container, without a database, and without a network and is
   therefore repeatable without preparation. A logging configuration under `src/test/resources` is allowed
   and does not stand in the way.
5. Time and inputs of a test program are parameters; it holds them as named constants in the test program or
   as test data under `src/test/resources`. Fixed inputs are allowed, a fixed expectation is not; it derives
   the expectation from its inputs. It does not use identifiers of the environment, such as the time zone,
   as a fixed value.
6. The verification command `mvn -q -DskipTests=false verify` runs the test programs and ends with the
   return value 0.

## Rationale

- JUnit 5 is already bound in the modules `client` and `util`; a change would bring no gain.
- The verification command evaluates only the return value; it therefore needs test programs that determine
  success and failure themselves, and not programs that only write their output.
- The verification command runs without an application container and without a database; a program that
  needs these is therefore not a test program in the sense of this decision.
- A `main` program that uses fixed calendar dates and fixed identifiers and opens connections is not
  repeatable without preparation and is therefore likewise not a test program.
- The test programs of a module are in the test scope and do not reach its jar.

## Alternatives

- Test programs without a framework as pure `main` programs – not selected; then every program would have to
  report its success itself instead of leaving it to the framework.
- Extending the verification command to all classes under `src/test/java`, also without a name pattern – not
  selected; many of them need an application container, a database, and a network and do not determine the
  return value reliably.

## Impact

- Files: `client/src/test/java` and `util/src/test/java` take the test programs.

## Open Points

- The Maven version that the build requires is not regulated by this decision; `maven-surefire-plugin` 3.6.0
  needs Maven 3.6.3 or newer, while the parent POM `org.jeesl.bom:bom-parent8` requires only 3.3.0.
- JUnit 4 has been removed from the repository; no module binds `junit:junit` and no source file imports
  `org.junit.*` (the migration is recorded in the change log of 2026-10-10).

## Evidence

### Implementation

- `pom.xml` (root) – binds `maven-surefire-plugin` 3.6.0; the modules inherit the version, and the plugin
  runs the test programs through the JUnit Platform
- `client/pom.xml`, `util/pom.xml`, `report/pom.xml`, `test/pom.xml` – bind
  `org.junit.jupiter:junit-jupiter-api` (test or provided scope) and no longer bind `junit:junit`;
  `xml/pom.xml`, `jsf/pom.xml`, `util/pom.xml`, and `report/pom.xml` add `junit-jupiter-engine` in the
  test scope (points 1 and 2)
- `client/src/test/java`, `util/src/test/java`, `report/src/test/java`, `xml/src/test/java` – carry test
  programs, that is classes with `@Test` methods; the shared base classes of `test/src/main/java` and the
  `IgnoreOtherRule` extension use JUnit 5 as well (points 1 to 3)
