---
id: ADR-0002
title: Maven as build tool
status: proposed
date: 2026-10-11
affects: []
related:
  - ADR-0001
  - ADR-0003
---

# ADR-0002: Maven as build tool

## Context

The build tool was recorded as an assumption (`README.md`, `## Technical Assumptions`), and `README.md`
(`## Open Fundamental Decisions`, item 3) leaves open whether the build settings of the POM files are pinned as a
constraint or as a decision. The choice to be decided is: which build tool builds the modules, resolves the
dependencies, and binds the extensions of parent, BOMs, and target platform, and whether that choice is pinned as
a decision.

## Decision

1. Maven builds the project; the root project `org.jeesl:jeesl` with packaging `pom` leads the 44 modules in one
   reactor (`pom.xml`, `<modules>`).
2. The build settings of the POM files are pinned as a decision and not as a constraint.
3. The packagings are `pom` for the root project and the module `xml`, `maven-plugin` for the module `maven`, and
   `jar` for the other modules.
4. The dependency versions come from the imported BOMs `bom-core` and `bom-eap73`; every module imports both BOMs
   itself.
5. The parent `org.jeesl.bom:bom-parent8` provides the plugin management and the enforcer rule
   `requireMavenVersion`.
6. Maven enforces selected dependencies through `maven-enforcer-plugin` (`bannedDependencies`) and creates the
   documentation through `maven-javadoc-plugin` in the `release` profile.
7. The minimum Maven version of the build is 3.6.3; `maven-surefire-plugin` 3.6.0 of the root `pom.xml` needs
   this version, while the parent `bom-parent8` enforces only 3.3.0.
8. A fixed Maven version with its own build runner is not set.

## Rationale

- Parent and BOMs come from the JEESL environment and are Maven artifacts.
- The packagings map the aggregator, the plugin, and the library artifacts onto Maven modules.
- The minimum Maven version 3.6.3 follows from `maven-surefire-plugin` 3.6.0 of the root `pom.xml`; the parent
  `bom-parent8` binds only the lower value 3.3.0, which does not suffice as the lower bound of the build.
- The parent binds the minimum version through `requireMavenVersion` and the modules inherit the check, so a
  fixed version with its own build runner is unnecessary.

## Alternatives

- Another build tool (Gradle) – not selected; parent, BOMs, and the artifact types are tailored to Maven.

## Impact

- Affected requirements: none.
- Files: `pom.xml`, the module POMs, and the parent POM `org.jeesl.bom:bom-parent8`.

## Open Points

- How the build enforces the minimum Maven version 3.6.3 is not regulated by this decision; the parent
  `bom-parent8` enforces only 3.3.0.
- The content of the parent POM `org.jeesl.bom:bom-parent8` and of the imported BOMs `bom-core` and `bom-eap73`
  is not regulated by this decision.

## Evidence

### Implementation

- `pom.xml` – the parent `org.jeesl.bom:bom-parent8`, packaging `pom`, and 44 `<module>` entries
- `pom.xml` – `maven-enforcer-plugin` with `bannedDependencies` and `maven-javadoc-plugin` in the `release`
  profile
- `util/pom.xml` and the other module POMs – packaging `jar` and the import of `bom-core` and `bom-eap73`
- `xml/pom.xml`, `maven/pom.xml` – the packagings `pom` and `maven-plugin`
- parent `org.jeesl.bom:bom-parent8` – `maven-enforcer-plugin` with `requireMavenVersion` `3.3.0`; the modules
  inherit the check

### Tests

- `mvn -o -N validate` on JDK 11 – `[SUCCESS]`; the enforcer executions `enforce-maven` (parent) and
  `enforce-banned-dependencies` (root) run

### Documentation

- `README.md` – names Maven (display location)
