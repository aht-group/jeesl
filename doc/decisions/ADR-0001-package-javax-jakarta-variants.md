---
id: ADR-0001
title: Build and publish the JAXB variants as classifier artifacts
status: accepted
date: 2026-10-08
---

# ADR-0001: Build and publish the JAXB variants as classifier artifacts

## Context

The project overview (`README.md`, `## Open Fundamental Decisions`, item 1) leaves the platform of the
next release open and records the two JAXB variants of the XML model as an assumption (`README.md`,
`## Technical Assumptions`). The open question is how the `xml` module produces its two JAXB variants and
which artifacts it publishes.

- The generated sources of the two variants are in `xml/src/main/javax` and `xml/src/main/jakarta` and
  declare the **same** fully qualified type names; only the annotation packages differ
  (`javax.xml.bind` and `jakarta.xml.bind`).
- A Maven module compiles one set of source roots into one output directory, so a single run could not
  carry both variants; the previous build selected the variant through the activation of a profile by
  the JDK that ran Maven (`xml/pom.xml`, profiles `autojavax` and `autojakarta`).
- The module published a jar without a classifier whose content depended on the JDK of the build, and
  each classifier jar copied that jar (`xml/pom.xml`, `maven-jar-plugin` in the profiles).

## Decision

The `xml` module builds both JAXB variants in one Maven run and publishes classifier artifacts only.

- The module uses `<packaging>pom</packaging>`: it publishes its POM and the classifier jars `javax`,
  `jakarta`, and `tests`, and no artifact without a classifier exists.
- `maven-compiler-plugin` (3.13.0) compiles both variants in one run, each with its own source roots and
  its own output directory; the properties `xml.classes.javax` (`${project.build.outputDirectory}`) and
  `xml.classes.jakarta` (`${project.build.directory}/classes-jakarta`) name them:
  - `compile-javax` at phase `compile` compiles `src/main/java` and `src/main/javax` with
    `--release 8` into `xml.classes.javax`.
  - `compile-jakarta` at phase `compile` compiles `src/main/java` and `src/main/jakarta` with
    `--release 11` into `xml.classes.jakarta`.
- `maven-jar-plugin` (3.3.0, managed by `org.jeesl.bom:bom-parent8`) creates the artifacts at phase
  `package`: the classifier `javax` from `xml.classes.javax`, the classifier `jakarta` from
  `xml.classes.jakarta`, and the classifier `tests` from the test classes.
- POM packaging binds no phase of the test build, so the module binds it itself: the test resources
  (`maven-resources-plugin`, goal `testResources` at phase `process-test-resources`), the test
  compilation (`maven-compiler-plugin`, goal `testCompile` at phase `test-compile`), the test run
  (`maven-surefire-plugin`, goal `test` at phase `test`), and the test jar (`maven-jar-plugin`, goal
  `test-jar` at phase `package`).
- The test compilation compiles against `xml.classes.javax`: the test base of the module
  (`xml/src/test/java/org/jeesl/AbstractXmlTest.java`) extends the shared base
  `net.sf.ahtutils.test.AbstractAhtUtilsXmlTest` of the `test` module, which marshals XML through a
  `javax`-bound helper and uses `javax.xml.datatype.XMLGregorianCalendar`; `javax` is the variant that
  the repository publishes for its Java EE platform.
- `maven-surefire-plugin` (`2.4.2`, managed by the root `pom.xml`) runs the test run of the module; the
  module names no version of its own.
- Consumers select the variant by classifier: `org.jeesl:jeesl-xml:<version>:javax` or `:jakarta`; the
  classifier `tests` carries the shared test classes (`util/pom.xml`, `doc/pom.xml`).
- The parent POM (`pom.xml`) manages `jeesl-xml` with the classifiers `javax`, `jakarta`, and `tests`; a
  dependency without a classifier is no longer managed there.
- The module build requires JDK 11 or newer, because `--release 11` compiles the `jakarta` variant; the
  `javax` artifact carries class-file version 52 and the `jakarta` artifact version 55.
- The profiles `javax` and `jakarta` only regenerate the JAXB sources from `xml/src/main/xsd/`
  (`cxf-xjc-plugin` 3.3.2 with `javax.xjb`, 4.0.0 with `jakarta.xjb`); they no longer select the build
  variant.
- The profile `jakarta` drops its `build-helper-maven-plugin` source root and its classifier jar; the
  profiles `autojavax` and `autojakarta` are removed.

## Rationale

- One command on one source revision produces both variants: `mvn -pl xml clean install`.
- The `maven-compiler-plugin` version is not the inherited one: the root `pom.xml` pins 3.8.0, which
  reports the parameters `compileSourceRoots` and `outputDirectory` as read-only; version 3.13.0 makes
  both writable per execution.
- Without an artifact that carries no classifier, a consumer cannot bind a variant by accident; the
  previous jar without a classifier changed its content with the JDK of the build.
- Separate output directories are what make both variants possible in one module run: the two source
  trees declare the same type names and cannot share one output directory.
- The `javax` variant writes into the module output directory, so the test build of the module and the
  consumers of the classifier `tests` compile against the variant that the repository publishes for its
  Java EE platform; the `jakarta` variant keeps an output directory of its own.
- The classifier jars remain ordinary attached artifacts, so `install`, `deploy`, and the signatures of
  the release profile need no handling of their own.
- `--release` pins the API level of the compilation instead of switching the JDK, so the `javax`
  artifact cannot use APIs that are newer than Java 8.
- Classifier jars keep the coordinates of the module, so the parent POM and the consumers change only by
  the classifier.

## Alternatives

### A variant selected by the JDK of the build (initial situation)

Keeps one module with jar packaging: the profile that the JDK activates adds one source root
(`xml/pom.xml`, `autojavax` and `autojakarta`), and the jar without a classifier carries the variant of
the build JDK; publishing both variants needs two builds on two JDKs.

### `packaging=jar` with a disabled main jar and `install-file`/`deploy-file`

Reaches the same repository content, but the deployment needs its own path, because `deploy-file` does
not sign the artifacts of the release profile.

### A JDK toolchain per compile execution

Compiles the `javax` artifact with a genuine JDK 8 and the `jakarta` artifact with a genuine JDK 11 in
one run. Requires an entry in the toolchain file of every build machine, and the toolchain does not pin
the API level.

### Two modules with distinct artifact IDs

Keeps ordinary jar packaging per variant, but changes the artifact ID and therefore the dependency of
every consumer.

### Eclipse Transformer

Compiles once and rewrites the bytecode of the `javax` jar into a `jakarta` jar. Requires one external
component, which needs a decision of its own, and the `jakarta` artifact would not be the product of the
Jakarta compiler.

## Impact

- `xml/pom.xml` holds the POM packaging, the two compile executions, the test executions, and the jar
  executions for the classifiers `javax`, `jakarta`, and `tests`.
- `xml/pom.xml` drops the profiles `autojavax` and `autojakarta` and the `build-helper-maven-plugin`
  source root and the classifier jar of the profile `jakarta`.
- `pom.xml` carries the dependency management of `jeesl-xml` with the classifiers `javax`, `jakarta`,
  and `tests` and no entry without a classifier.
- `client/pom.xml`, `connectors/pom.xml`, `report/pom.xml`, `system-security/pom.xml`, and
  `util/pom.xml` name the classifier `javax`; `connectors/pom.xml` drops its second dependency on
  `jeesl-xml` without a classifier.
- `util/pom.xml` and `doc/pom.xml` name the classifier `tests`.
- `README.md` (`## Technical Assumptions`, `## Build and Start`) and the architecture file
  (`doc/requirements/architecture.md`) display the decision; the index (`doc/status.md`) lists it.
- A build of the `xml` module with JDK 8 no longer succeeds.

## Open Points

- The Java version that binds the whole repository build is not regulated by this decision; ADR-0002
  governs the lower bound of the whole build, and this module needs JDK 11 or newer (`--release 11`).
- POM packaging binds no main resources either (`maven-resources-plugin`, goal `resources`); a later
  `src/main/resources` is copied nowhere and needs its own binding and a copy per variant.
- Consumers outside this repository that resolve `org.jeesl:jeesl-xml` without a classifier are not
  regulated by this decision; their change to a classifier is left to them.
- How a consumer separates the two variants on its classpath is not regulated by this decision; the
  classes of `src/main/java` are in both jars, so `javax` and `jakarta` cannot share a classpath.
- Which sources and javadoc artifacts a release publishes per classifier; `maven-source-plugin` skips a
  POM packaging.
- Whether the `jakarta` variant receives a test run of its own; the shared test base is bound to
  `javax`.
- The content of the two regeneration profiles (`xjc-clean` file sets, the referenced XSD) is not
  regulated by this decision.

## Evidence

### Implementation

- `xml/pom.xml` – open: the POM packaging, the two compile executions, the test executions, and the jar executions
- `pom.xml` (root) – open: the dependency management of `jeesl-xml` with the classifiers `javax`,
  `jakarta`, and `tests`
- `client/pom.xml`, `connectors/pom.xml`, `report/pom.xml`, `system-security/pom.xml`, and `util/pom.xml`
  – open: the classifier `javax`
- `util/pom.xml` and `doc/pom.xml` – open: the classifier `tests`

### Tests

- `mvn -pl xml clean install` on JDK 11 – open: the classifier jars `javax`, `jakarta`, and `tests` and
  no artifact without a classifier
- `mvn test` of the reactor with a consumer of the classifier `jakarta` – open: the variant used

### Documentation

- `README.md` (`## Technical Assumptions`, `## Build and Start`) – open: the platform entry and the JDK
  prerequisite
- `doc/requirements/architecture.md` – open: the decision is displayed
- `doc/status.md` – open: the index lists the decision

