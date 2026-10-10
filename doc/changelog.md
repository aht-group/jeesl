# Change Log

## 2026-10-10 – Build: drop the JUnit 4 ignore listener and the redundant surefire block

- What: Removed the JUnit 4 `RunListener` `net.sf.ahtutils.test.PrintIgnoreRunListener` and its surefire
  configuration in `util/pom.xml`. The root `pom.xml` is now the only place that names
  `maven-surefire-plugin`; only `xml` keeps its own binding.
- Result: Every `jar` module inherits the plugin from the root; the `util` test run still uses
  `surefire:3.6.0`, and the 24 pre-existing `log4j` errors stay.
- Evidence: `mvn -pl util clean test` on JDK 11 – 59 test programs through the inherited plugin;
  `mvn -pl test compile` – BUILD SUCCESS; no reference to the removed class remains.
- Files: test/src/main/java/net/sf/ahtutils/test/PrintIgnoreRunListener.java (removed), util/pom.xml,
  doc/changelog.md.

## 2026-10-10 – Build: maven-surefire-plugin raised to 3.6.0

- What: Raised `maven-surefire-plugin` to 3.6.0 in the root `pom.xml` and dropped the redundant version
  override in `util/pom.xml` (it inherits the version now); the test run uses the JUnit Platform and executes
  the JUnit 5 test programs of ADR-0003.
- Result: The `xml` module is green; the JUnit 5 test programs run. The pre-existing `log4j` errors of `util`
  and `client` stay unchanged and are not caused by the plugin.
- Evidence: `mvn -pl xml test` on JDK 11 – 274 test programs, 0 failures, 0 errors, BUILD SUCCESS;
  `mvn -pl util test` and `mvn -pl client test` – the same errors as under 2.11.
- Files: pom.xml, util/pom.xml, README.md, doc/decisions/ADR-0001-package-javax-jakarta-variants.md,
  doc/decisions/ADR-0003-test-strategy.md, doc/changelog.md.

## 2026-10-10 – Tests: ADR-0003 for the test strategy

- What: Adapted the test strategy copied from another project and recorded it as ADR-0003: test programs run
  with JUnit 5. Removed the references of the source project (its constraint, its evidence file, its class
  and module names), set the status to `proposed`, and named the open points (the bound `maven-surefire-plugin`
  and the JUnit 4 dependency).
- Result: The decision is recorded as a draft; the index lists it.
- Evidence: `client/pom.xml` and `util/pom.xml` bind `org.junit.jupiter:junit-jupiter-api` in the test scope;
  the test trees of `client` and `util` carry classes with `@Test` methods.
- Files: doc/decisions/ADR-0003-test-strategy.md, doc/status.md, doc/changelog.md.

## 2026-10-10 – Requirements: FR-001 for the CLI option handler

- What: Recorded FR-001 for the reusable help, logging and configuration handling of the entry points.
- Result: Review "Approval recommended" (0 findings against the recommendation, 2 notes); approved on instruction.
- Evidence: The five repository patterns – no match; the line width and duplicate checks – no match.
- Files: doc/requirements/functional/FR-001-cli-support.md, doc/status.md, doc/changelog.md.

## 2026-10-08 – XML/Build: ADR-0001 and ADR-0002 implemented

- What: ADR-0001 implemented (`xml/pom.xml` as POM packaging with the classifier artifacts `javax`,
  `jakarta`, and `tests`, the classifier `javax` for the consumers of the model) and ADR-0002 implemented
  (`release` `8` in the root `pom.xml`, `test/pom.xml` without `source` and `target`).
- Result: Acceptance after implementation: the reviewed text is unchanged, so the findings of ADR-0001
  remain as accepted on instruction (`F1`, `F3`, and two notes open); ADR-0002 carries no open finding.
- Evidence: `mvn -pl xml clean install -DskipTests` on JDK 11 – the three classifier jars and no artifact
  without a classifier (52/55); `mvn -DskipTests clean install` of the reactor – all 45 modules build;
  `mvn -pl api -am clean install` and `mvn -pl util,doc -am test-compile` – the consumers resolve;
  `mvn -pl xml test` – 274 test programs against the `javax` classes with the 20 failures that also occur
  on JDK 8.
- Files: xml/pom.xml, pom.xml, test/pom.xml, client/pom.xml, connectors/pom.xml, report/pom.xml,
  system-security/pom.xml, util/pom.xml, README.md, doc/requirements/architecture.md,
  doc/decisions/ADR-0001-package-javax-jakarta-variants.md, doc/decisions/ADR-0002-java-compile-level.md.

## 2026-10-08 – XML: ADR-0001 revised and accepted

- What: ADR-0001 revised and accepted on instruction (open points, profiles, surefire, evidence).
- Result: Review "Approval not recommended" (3 findings, 8 notes); accepted on instruction with F1
  (another repository), F3 (consumption) and two notes open.
- Evidence: Probe with POM packaging: no `resources` goal; `mvn test` resolves `jakarta` to the javax
  classes.
- Files: doc/decisions/ADR-0001-package-javax-jakarta-variants.md, doc/status.md, doc/changelog.md.

## 2026-10-08 – Build: ADR-0002 corrected and accepted

- What: ADR-0002 corrected: the `xml` values and the evidence lines.
- Result: Review "Approval recommended" (0 findings against the recommendation, 0 notes); ADR-0002 precedes
  ADR-0001 on instruction and is accepted.
- Evidence: `mvn test-compile` with the value `8` on JDK 11 – [SUCCESS]; `List.of` under `--release 8` fails;
  the review patterns and the line width – no match.
- Files: doc/decisions/ADR-0002-java-compile-level.md, doc/status.md, doc/changelog.md.

## 2026-10-08 – Build: ADR-0002 for the Java 8 compile level

- What: Imported ADR-0003 of the EXLP project as ADR-0002: every module compiles against the Java 8 API.
- Result: Review "Approval recommended" (0 findings against the recommendation, 1 note).
  ADR-0002 is recorded as proposed; the index lists it.
- Evidence: On JDK 11, `release` 8 rejects `List.of`;
  `source` and `target` 1.8 compile it with class-file version 52.
- Files: doc/decisions/ADR-0002-java-compile-level.md, doc/status.md, doc/changelog.md.

## 2026-10-08 – XML: decision and index for the JAXB variants

- What: Recorded ADR-0001 for the two JAXB variants of `jeesl-xml`, created the index and the activity
  log, and set the decision to proposed on instruction.
- Result: Review "Approval recommended" (0 findings against the recommendation, 2 notes decided); the
  decision is recorded and the index lists it.
- Evidence: Section order and line width without a match.
- Files: doc/decisions/ADR-0001-package-javax-jakarta-variants.md, doc/status.md, doc/changelog.md.
