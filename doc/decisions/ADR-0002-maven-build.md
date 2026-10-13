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
5. The parent `org.jeesl.bom:bom-parent8` provides the plugin management; the aggregator `org.jeesl.bom:bom`
   above it provides the plugin management of `maven-enforcer-plugin` and the enforcer rule `requireMavenVersion`.
6. Maven enforces selected dependencies through `maven-enforcer-plugin` (`bannedDependencies`) and creates the
   documentation through `maven-javadoc-plugin` in the `release` profile.
7. The minimum Maven version of the build is 3.6.3; the aggregator `org.jeesl.bom:bom` enforces this value
   through `requireMavenVersion` for every module of the reactor.
8. A fixed Maven version with its own build runner is not set.

## Rationale

- Parent and BOMs come from the JEESL environment and are Maven artifacts.
- The packagings map the aggregator, the plugin, and the library artifacts onto Maven modules.
- The minimum Maven version 3.6.3 is the value that the aggregator `org.jeesl.bom:bom` enforces through
  `requireMavenVersion`; the plugins of the build (`maven-compiler-plugin` 3.16.0, `maven-surefire-plugin` 3.6.0)
  name the same value as their own prerequisite.
- The aggregator binds the minimum version and every module inherits the check, so a fixed version with its own
  build runner is unnecessary.

## Alternatives

- Another build tool (Gradle) – not selected; parent, BOMs, and the artifact types are tailored to Maven.

## Impact

- Affected requirements: none.
- Files: `pom.xml`, the module POMs, the parent POM `org.jeesl.bom:bom-parent8`, and the aggregator
  `org.jeesl.bom:bom`.

## Open Points

- The content of the aggregator `org.jeesl.bom:bom`, of the parent POM `org.jeesl.bom:bom-parent8`, and of the
  imported BOMs `bom-core` and `bom-eap73` is not regulated by this decision.

## Evidence

### Implementation

- `pom.xml` – the parent `org.jeesl.bom:bom-parent8`, packaging `pom`, and 44 `<module>` entries
- `pom.xml` – `maven-enforcer-plugin` with `bannedDependencies` and `maven-javadoc-plugin` in the `release`
  profile
- `util/pom.xml` and the other module POMs – packaging `jar` and the import of `bom-core` and `bom-eap73`
- `xml/pom.xml`, `maven/pom.xml` – the packagings `pom` and `maven-plugin`
- aggregator `org.jeesl.bom:bom` – `maven-enforcer-plugin` `1.4.1` with the rule `requireMavenVersion` `3.6.3`;
  every module inherits the check

### Tests

- `mvn -o validate` – `[SUCCESS]` for the 45 modules; the enforcer executions `enforce-maven` (aggregator) and
  `enforce-banned-dependencies` (root) run with `maven-enforcer-plugin` 1.4.1

### Documentation

- `README.md` – names Maven (display location)
