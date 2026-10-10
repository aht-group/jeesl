# JEESL (JavaEE Support Library)

JEESL is a library of utilities and helpers for the development of JavaEE applications. It is maintained together with the surrounding toolchain (OpenFuXML, MetaChart) and is published as `org.jeesl:jeesl` from `https://github.com/aht-group/jeesl`.

This file is the **Project Overview** and the entry point for people: it names the project objective, the current status, the project values, and the project-specific roles. The rules for the AI assistant are in `doc/llm.md`; the rules and forms for requirements are in `doc/requirements/requirements.md`.

## Initial Situation

JEESL was started as a reusable foundation for JavaEE projects, so that recurring tasks are implemented once instead of anew in every application. From this, the project grew over many years into a multi-module Maven reactor with 44 modules (root `pom.xml`, `<modules>`) that is consumed as a set of libraries.

The modules fall into four groups:

| Group | Modules | Content |
|---|---|---|
| Foundation | `interfaces`, `xml`, `api`, `util`, `ejb`, `jsf`, `client`, `cycle` | Technology-independent interfaces, the XML model generated from XSD, facade/REST/bean contracts, general utilities, the JPA/EJB implementation layer, the JSF layer, and the REST client |
| Tooling | `maven`, `test`, `doc`, `report`, `connectors`, `monitor`, `prototype` | Maven plugins (Mojos), test base classes and assertions, document generation (OpenFuXML, LaTeX, Word), reporting (Excel, JasperReports), connector implementations, monitoring and evaluation, and a prototype web application |
| `io-*` | `io-ai`, `io-attribute`, `io-cms`, `io-crypto`, `io-db`, `io-fr`, `io-label`, `io-locale`, `io-log`, `io-mail`, `io-maven`, `io-report`, `io-ssi` | Reusable infrastructure features: persistent models, converters, handlers, lazy models, web controllers, REST handlers |
| `system-*` and `module-*` | `system-bookmark`, `system-constraint`, `system-feedback`, `system-filter`, `system-job`, `system-property`, `system-security`, `system-tenant`, `module-aom`, `module-cal`, `module-cl`, `module-hd`, `module-mmg`, `module-news`, `module-tafu`, `module-ts` | Optional system functions and ready-made business modules |

A consuming application depends on the modules it needs; JEESL itself has no standalone deliverable.

## Status

- The sources correspond to the upstream state of branch `master`.
- The documentation scaffolding has been introduced: `doc/llm.md` (**Project Instructions**) and `doc/requirements/requirements.md` (**Requirements Rulebook**).
- The requirements themselves, the constraints (`doc/requirements/constraints/`), the **Decision Repository** (`doc/decisions/`), the **Index File** (`doc/status.md`) and the **Activity Log File** (`doc/changelog.md`) are not populated yet; `doc/requirements/architecture.md` still contains empty sections.
- The **AI tool rule files** (`.clinerules/`) named in this file do not yet exist in the repository.

## Relevant Values

This table names the values that are relevant for the project and the location that is authoritative for each of them.

| Item | Value | Authoritative location |
|---|---|---|
| Document language | English | this file |
| Group ID | `org.jeesl` | `pom.xml` |
| Artifact ID | `jeesl` | `pom.xml` |
| Version | `0.3.2-SNAPSHOT` | `pom.xml`, property `maven.version.jeesl.bom` |
| Parent | `org.jeesl.bom:bom-parent8:0.3.2-SNAPSHOT`, relative path `../bom/parent8` | `pom.xml` |
| Imported BOMs | `bom-core`, `bom-eap73` | `pom.xml` and the module POM files |
| Java compiler | `release` `8`, encoding `UTF-8` | `pom.xml` |
| Publication | Sonatype Central, publishing server id `central` | `pom.xml` |
| SCM | `https://github.com/aht-group/jeesl.git`, branch `master` | `pom.xml` |
| Credentials | This repository stores no production credentials. Database, mail, and external-service credentials are read at runtime from the configuration of the consuming application, for example in `org.jeesl.controller.io.db.sql.SqlConnectionFactory`. | this file |
| Working Repository | not defined yet | this file |
| Staging Repository | not defined yet | this file |

## Technical Assumptions

These points are the result of an analysis of the existing build files and sources. They are **assumptions** and are not pinned yet by a **constraint** or a **decision** (`doc/requirements/constraints/`, `doc/decisions/`).

- Language and platform: Java 8 API; the compilation uses `<release>8</release>`, so the API of Java 8 is the lower bound of the reactor, and `jeesl-xml` compiles its `jakarta` artifact with `--release 11`, which makes JDK 11 or newer the JDK of the build.
- Namespace: Java EE with the `javax` namespace; `jeesl-xml` publishes the two JAXB variants as the classifier artifacts `javax` and `jakarta` and the shared test classes as the classifier `tests`.
- Target container: JBoss EAP 7.x; the imported BOM `bom-eap73` selects the matching platform dependencies.
- Build: Apache Maven, multi-module reactor; the plugin module `maven/` (`jeesl-maven`) declares Maven 3.1 as prerequisite; there is no Maven Wrapper.
- Persistence: JPA/Hibernate on EJB, with datasource and Hibernate configuration templates for JBoss EAP under `maven/src/main/resources/jeesl/system/io/config/jboss/eap/`.
- Web: JSF and JAX-RS (REST); servlets and JSF components live in `jsf/`.
- XML: JAXB models and code generation from 36 XSD files in `xml/src/main/xsd/`.
- Logging: SLF4J; the test resources use log4j configurations under `src/test/resources/config.*/log4j.xml`.
- Tests: JUnit 5 with shared test bases such as `net.sf.ahtutils.test.AbstractAhtUtilsXmlTest`, `org.jeesl.test.AbstractJeeslXmlTest` and `org.jeesl.test.JeeslAssert`, plus the extension `net.sf.ahtutils.test.IgnoreOtherRule`.
- Libraries in use: EXLP (`net.sf.exlp`), OpenFuXML (`org.openfuxml`), MetaChart (`org.metachart`), Apache POI, Aspose Words, Apache Batik, Infinispan, Apache Commons Configuration 2.
- `commons-logging:commons-logging` is excluded and banned by the Maven Enforcer plugin.

## Open Fundamental Decisions

The following questions are open and must be answered by the responsible person before a requirement or a decision can be derived from them:

1. Which platform is the target of the next release: JBoss EAP 7.x with `javax` and Java 8 (today's default, `bom-eap73`) or EAP 8 with `jakarta` and a newer Java version?
2. Which Java version is binding for the build?
3. Are the build settings that are documented in the POM files to be pinned as a **constraint** (`doc/requirements/constraints/`) or as a **decision** (`doc/decisions/`), and is the POM then only a display location?
4. Where are the **AI tool rule files** located? This file names `.clinerules/` and `.clinerules/10-git-commit.md`, but neither is present in the repository.
5. Do `jeesl-doc` (OpenFuXML, LaTeX, Aspose Words) and `jeesl-prototype` remain part of the release build?
6. Which directories act as the **Working Repository** and the **Staging Repository**?

## Decided Fundamental Decisions

The following fundamental decisions are already in effect:

- The project documents are written in **English** (document language).
- The repository is built as a single multi-module Maven reactor; the modules are listed in the root `pom.xml`.

All other decisions are kept in the **Decision Repository** (`doc/decisions/`) with a number and a status; the repository is not populated yet.

## Planned Milestones

1. Populate the documentation scaffolding: **Decision Repository** (`doc/decisions/`), **constraints** (`doc/requirements/constraints/`), **Index File** (`doc/status.md`) and **Activity Log File** (`doc/changelog.md`).
2. Pin the build environment (Java, Maven, platform, namespace) as a **constraint** or a **decision**.
3. Describe the existing architecture in `doc/requirements/architecture.md` (structure, modules, components and patterns, flow, technology) and reference the authoritative locations.
4. Derive functional and non-functional requirements from the implemented modules and record them with their status and evidence.
5. Ongoing: cover new functionality with tests and record every change in the **Activity Log File**.

## Build and Start

Prerequisites:

- JDK 11 or newer; `jeesl-xml` compiles its `jakarta` artifact with `--release 11`, and every module compiles against the Java 8 API (`--release 8`).
- Apache Maven 3.6.3 or newer; the repository contains no Maven Wrapper.
- The parent POM `org.jeesl.bom:bom-parent8` and the imported BOMs `bom-core` and `bom-eap73` must be resolvable. The root `pom.xml` refers to the sibling directory `../bom/parent8`; if that checkout is absent, Maven falls back to the local or the remote repository.
- Access to Maven Central, the Sonatype snapshot repository and the OSGeo release repository (`pom.xml`, `<repositories>`).

Build all modules and install them into the local repository:

```
mvn clean install
```

Further commands:

```
mvn clean install -DskipTests       # without tests
mvn -pl xml clean install           # both JAXB variants as the classifier artifacts javax and jakarta
mvn -pl jsf -am clean install       # one module together with its dependencies
mvn clean deploy                    # publication via central-publishing-maven-plugin (server id: central)
mvn clean verify -Prelease          # additionally sources, javadoc and GPG signing
```

Start:

JEESL is a library and has no standalone start; a consuming application deploys the modules it needs on a Java EE container. Command-line entry points exist in the test scope, for example `net.sf.ahtutils.monitor.CliMonitoringWorker` and `net.sf.ahtutils.monitor.CliAnalysisApp` in `jeesl-monitor` and `org.jeesl.client.app.NettyServer` in `jeesl-connectors`; they are started from an IDE or with the test classpath.

## Development Principles

- Follow the project instructions for the AI assistant in `doc/llm.md` and the **Requirements Rules** in `doc/requirements/requirements.md`.
- Write documents, source code, and new file names in **English**; use ASCII kebab-case for new file names.
- Cover new functionality with tests and run them (together with the Git diff) after each change.
- Never create a commit automatically; see the **Commit Message Rule**.

## Rules and Roles

- Project instructions for the AI assistant: `doc/llm.md`; rule files of the AI tool: `.clinerules/`.
- Commit Message Rule: `.clinerules/10-git-commit.md`.
- Requirements Rules: `doc/requirements/requirements.md`.
- Index File of requirements and architecture decisions including status: `doc/status.md`.
- Change Log (chronicle): `doc/changelog.md`.
- Architecture File: `doc/requirements/architecture.md`; Decision Repository: `doc/decisions/`.
- Requirements Repository: `doc/requirements/` with the categories `functional/`, `non-functional/`, `security/`, `usability/` and `constraints/`.
- Build Tools: the Maven build in the root `pom.xml` together with the plugin module `maven/` (`jeesl-maven`).
- Working Repository: not defined yet (see **Open Fundamental Decisions**).
- Staging Repository: not defined yet (see **Open Fundamental Decisions**).