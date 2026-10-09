---
id: ADR-0002
title: Compile all modules against the Java 8 API
status: accepted
date: 2026-10-08
related:
  - ADR-0001 (precondition)
---

# ADR-0002: Compile all modules against the Java 8 API

## Context

The project overview (`README.md`, `## Open Fundamental Decisions`, item 2) leaves the Java version that
binds the build open and records Java 8 as an assumption (`README.md`, `## Technical Assumptions`,
`## Relevant Values`). The parent POM `org.jeesl.bom:bom-parent8` and the root `pom.xml` configure
`maven-compiler-plugin` with `source` and `target` `1.8`. The open question is how the artifacts of the
modules of the reactor keep running on Java 8 although a newer JDK compiles them.

- `source` and `target` select the source level and the class-file version, but the compilation uses the
  API of the JDK that runs the compiler.
- A build on JDK 11 could therefore use types and members that Java 8 does not have; the class files
  stay at version 52, and the failure appears only at runtime on Java 8.

ADR-0001 sets out the values of the `xml` module; this decision presupposes that decision and does not repeat
its values.

## Decision

Every module of the repository compiles against the Java 8 API.

- The root `pom.xml` configures `maven-compiler-plugin` with the parameter `release` and the value `8`;
  it drops its own parameters `source` and `target` `1.8`.
- The parameter `release` requires `maven-compiler-plugin` 3.6.0 or newer; the version that the root
  `pom.xml` manages applies.
- While `release` is set, the compilation uses the API data of Java 8 and writes class-file version 52;
  the compiler call carries `--release 8` and no value of `source` or `target`.
- The module `test` (`test/pom.xml`) drops its own parameters `source` and `target` `1.8` and inherits
  the value of the root `pom.xml`.
- The module `xml` compiles its `javax` artifact with `--release 8` and its `jakarta` artifact with
  `--release 11`; the configuration of an execution overrides the inherited value (ADR-0001).
- While the `xml` module carries no value of its own, the value `8` of the root `pom.xml` applies to the
  variant that the build selects, in the artifact and in the test compilation alike.
- The build requires JDK 9 or newer for the `release` argument, in practice JDK 11 or newer (ADR-0001).
- The artifacts of the modules of the reactor carry class-file version 52 and use the Java 8 API only;
  the test compilation is checked in the same way.

## Rationale

- `--release` selects the API data of that Java version, so the compiler rejects types and members that
  the version does not provide.
- One value in the root `pom.xml` covers the reactor; a module with its own value states it itself.
- The failure appears while compiling instead of in the runtime of a Java 8 consumer; the target
  container is JBoss EAP 7.x (`README.md`, `## Technical Assumptions`).

## Alternatives

### `source` and `target` `1.8` (initial situation)

Keeps class-file version 52 but compiles against the API of the build JDK, so a Java 9 or newer API
stays unnoticed.

### Building with JDK 8

Rejects a newer API by definition, but ADR-0001 requires JDK 11 or newer for the `xml` module, so the
repository cannot be built with a JDK 8 throughout.

### Signature check of the Java 8 API after the compilation

Checks the bytecode and works with any JDK, but adds an external component, which requires a decision of
its own, and a separate signature artifact.

## Impact

- `pom.xml` (root) – the parameter `release` replaces `source` and `target` for every module, and the managed
  `maven-compiler-plugin` carries a version of 3.6.0 or newer.
- `test/pom.xml` – the module drops its own `source` and `target` configuration.
- `xml/pom.xml` – no change follows from this decision; the module carries the values of its two artifacts
  (ADR-0001).
- `README.md` (`## Relevant Values`) – the compiler entry names the value `8` of the parameter `release`.
- `README.md` (`## Technical Assumptions`) – the language entry names the Java 8 API of the compilation.
- `README.md` (`## Build and Start`) – the prerequisite names JDK 11 or newer in place of JDK 8.
- `doc/requirements/architecture.md` displays the decision.
- The index (`doc/status.md`) lists the decision.

## Open Points

- A module that later needs Java 11 APIs configures its own `release` value; the decision does not
  regulate which module that is.
- The parameters `source` and `target` `1.8` of the parent POM `org.jeesl.bom:bom-parent8` stay unchanged, and
  the decision does not regulate that project; the value of the root `pom.xml` supersedes them.
- The JDK that runs the build is not regulated beyond the minimum that the `release` argument requires
  (`README.md`, `## Open Fundamental Decisions`, item 2).

## Evidence

### Implementation

- `pom.xml` (root) and `test/pom.xml` – open: the parameter `release` and the dropped `source` and `target` values

### Tests

- `javac` with `--release 8` on a class that uses `List.of` – open: the compilation fails with `cannot find symbol`
- `mvn test-compile` of the reactor on JDK 11 – open: class files of version 52

### Documentation

- `README.md` (`## Relevant Values`, `## Technical Assumptions`) – open: the compiler entry and the language entry
- `README.md` (`## Build and Start`) – open: the JDK prerequisite
- `doc/requirements/architecture.md` – open: the decision is displayed
