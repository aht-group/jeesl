# Change Log

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
