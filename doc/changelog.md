# Change Log

## 2026-10-10 – Requirements: remove the redundant Decided Questions of FR-001

- What: Removed the FR-001 Decided Questions that repeat facts already carried by `## Requirement`,
  `## Scope`, the acceptance criteria, or the YAML header, on instruction; renumbered the remaining entry.
- Result: FR-001 keeps one Decided Question.
- Evidence: The line width, duplicate, and attribute checks on FR-001 – no match.
- Files: doc/requirements/functional/FR-001-cli-support.md, doc/changelog.md.

## 2026-10-10 – Tests: every test class runs on JUnit 5

- What: Migrated the JUnit 4 test classes of `test`, `util`, `report`, and `xml` to JUnit 5 and removed every
  `junit:junit` dependency.
- Result: No source imports `org.junit.*`; the six test modules run green.
- Evidence: `mvn -o -pl test,util,xml,jsf,client,doc,report test` on JDK 11 – BUILD SUCCESS, 0 failures and
  0 errors.
- Files: test/, util/, report/, xml/, the module POMs, README.md, doc/decisions/ADR-0003-test-strategy.md.

## 2026-10-10 – Tests: maven test base initializes the target directory and logs via log4j2

- What: `AbstractUtilsMavenTst` dereferenced an unassigned `fTarget` in `initFile`; the assignment and the
  `setfTarget` helper were added, and `maven/pom.xml` gained the `log4j-slf4j2-impl` test dependency.
- Result: The maven test run is green.
- Evidence: `mvn -o -pl maven clean test` on JDK 11 – 9 tests, 0 failures, 0 errors, 4 skipped.
- Files: maven/pom.xml, maven/src/test/java/net/sf/ahtutils/test/AbstractUtilsMavenTst.java, doc/changelog.md.

## 2026-10-10 – Tests: doc test base uses the JUnit 5 assertEquals argument order

- What: `AbstractUtilsDocTest` passed the message first in its three `Assertions.assertEquals` calls; the
  arguments were reordered, and `TestOfxStatusTableFactory` initializes its `config` field in `init()`.
- Result: The doc test run is green.
- Evidence: `mvn -o -pl doc clean test` on JDK 11 – 23 tests, 0 failures, 0 errors, 7 skipped.
- Files: doc/src/test/java/net/sf/ahtutils/, doc/changelog.md.

## 2026-10-10 – Tests: logging initialization moved to log4j2

- What: Replaced the log4j 1.x `LoggerInit` in the test bases of `test`, `util`, `client`, `maven`, `jsf`,
  `doc`, and `report` with `LoggerBootstrap` (log4j 2).
- Result: The `NoClassDefFoundError` of `org.apache.log4j.Logger` is gone; the `util` and `client` runs are
  green.
- Evidence: `mvn -o -pl util clean test` on JDK 11 – 118 tests; `mvn -o -pl client clean test` – 33 tests,
  0 failures, 0 errors.
- Files: the test bases of the seven modules, util/, doc/changelog.md.

## 2026-10-10 – Build: drop the JUnit 4 ignore listener and the redundant surefire block

- What: Removed the JUnit 4 `RunListener` `PrintIgnoreRunListener` and its surefire configuration in
  `util/pom.xml`; the root `pom.xml` is the only place that names `maven-surefire-plugin`.
- Result: Every `jar` module inherits the plugin from the root.
- Evidence: `mvn -pl util clean test` on JDK 11 – 59 tests through the inherited plugin; `mvn -pl test
  compile` – BUILD SUCCESS.
- Files: test/ (removed `PrintIgnoreRunListener`), util/pom.xml, doc/changelog.md.

## 2026-10-10 – Build: maven-surefire-plugin raised to 3.6.0

- What: Raised `maven-surefire-plugin` to 3.6.0 in the root `pom.xml` and dropped the version override in
  `util/pom.xml`.
- Result: The `xml` module is green and the JUnit 5 test programs run.
- Evidence: `mvn -pl xml test` on JDK 11 – 274 tests, 0 failures, 0 errors, BUILD SUCCESS.
- Files: pom.xml, util/pom.xml, README.md, the ADR-0001 and ADR-0003 files, doc/changelog.md.

## 2026-10-10 – Tests: ADR-0003 for the test strategy

- What: Adapted the test strategy of another project and recorded it as ADR-0003: test programs run with
  JUnit 5. Removed the references of the source project and set the status to `proposed`.
- Result: The decision is recorded as a draft; the index lists it.
- Evidence: `client/pom.xml` and `util/pom.xml` bind `junit-jupiter-api` in the test scope.
- Files: doc/decisions/ADR-0003-test-strategy.md, doc/status.md, doc/changelog.md.

## 2026-10-10 – Requirements: FR-001 for the CLI option handler

- What: Recorded FR-001 on the reusable help, logging and configuration handling of the entry points.
- Result: Review "Approval recommended" (0 findings against the recommendation, 2 notes); approved on instruction.
- Evidence: The five repository patterns – no match; the line width and duplicate checks – no match.
- Files: doc/requirements/functional/FR-001-cli-support.md, doc/status.md, doc/changelog.md.

## 2026-10-08 – XML/Build: ADR-0001 and ADR-0002 implemented

- What: Implemented ADR-0001 (the `xml` classifiers `javax`, `jakarta`, and `tests`) and ADR-0002 (`release`
  `8` in the root `pom.xml`).
- Result: The reactor builds; the ADR-0001 findings `F1`, `F3`, and two notes stay open.
- Evidence: `mvn -DskipTests clean install` on JDK 11 – all modules build; `mvn -pl xml test` – 274 test
  programs (20 pre-existing failures as on JDK 8).
- Files: xml/pom.xml, pom.xml, README.md, architecture.md, the two ADR files, changelog.md.

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

- What: Recorded ADR-0001 on the two JAXB variants of `jeesl-xml`, created the index and the activity
  log, and set the decision to proposed on instruction.
- Result: Review "Approval recommended" (0 findings against the recommendation, 2 notes decided); the
  decision is recorded and the index lists it.
- Evidence: Section order and line width without a match.
- Files: doc/decisions/ADR-0001-package-javax-jakarta-variants.md, doc/status.md, doc/changelog.md.
