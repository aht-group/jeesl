# Technical Architecture

## Structure

JEESL is a multi-module Maven build.
The repository root holds one aggregator POM that lists every module.
Each module keeps its own POM, sources and resources.

The parent POM and the platform BOMs that the modules import are maintained outside this repository.

The modules are arranged in three layers.
The foundation holds `interfaces`, `xml`, `api`, `util`, `ejb`, `jsf` and `client`.
The tooling holds `maven`, `test`, `doc`, `report`, `connectors`, `monitor`, `prototype` and `cycle`.
The features hold the `io-*`, the `system-*` and the `module-*` modules and build on the foundation.

A feature module repeats one package layout:

```text
org.jeesl.model.ejb.<area>.<feature>        JPA entities
org.jeesl.controller.converter.<area>...    JSF converters
org.jeesl.controller.handler.<area>...      lazy models, REST handlers, tuple handlers
org.jeesl.controller.web.<area>...          web controllers
src/main/resources/META-INF/resources/...   XHTML views
src/main/resources/jeesl/.../db/migration/  ordered SQL migration scripts
```

The XML model is generated rather than written by hand.
The XSD files under `xml/src/main/xsd` are the source.
XJC produces the JAXB classes into the `javax` and the `jakarta` source folder.

## Modules

| Artifact | Role |
|---|---|
| `jeesl-interfaces` | Model contracts, marker interfaces, and the interfaces of facade, handler and controller |
| `jeesl-xml` | JAXB model generated from the XSD |
| `jeesl-api` | Facade, bean, handler and REST contracts of the features |
| `jeesl-util` | Factories, processors, query helpers, comparators and database access |
| `jeesl-ejb` | Facade implementations on JPA, and REST handlers |
| `jeesl-jsf` | JSF components, handlers and controllers |
| `jeesl-client` | REST client, client models and client application |
| `jeesl-maven` | Maven plugin for generation and deployment support |
| `jeesl-test` | Test base classes, assertions and test rules |
| `jeesl-doc` | Document, diagram and icon generation |
| `jeesl-report` | Reporting and the Excel based import and export |
| `jeesl-connectors` | Connectors to external systems |
| `jeesl-monitor` | Monitoring, measurement and evaluation |
| `jeesl-prototype` | Prototype web application |
| `jeesl-cycle` | Entity `SystemJobStatus` for modules that use it without depending on `system-job` |
| `jeesl-io-*` | Features of the `io` area in the layout above |
| `jeesl-system-*` | Features of the `system` area in the layout above |
| `jeesl-module-*` | Business modules in the layout above |

## Components and Patterns

- The package `org.jeesl.interfaces.model` holds generic contracts such as `JeeslLang` and `JeeslStatus`.
- The tenant support of an entity is declared through `JeeslTenantRealm` and `JeeslWithTenantSupport`.
- The package `org.jeesl.interfaces.model.marker.jpa` declares the JPA markers,
  among them `EjbPersistable`, `EjbSaveable`, `EjbMergeable` and `EjbRemoveable`.
- The interfaces under `org.jeesl.interfaces.model.with` describe single abilities of an entity.
- These abilities cover code, position, validity, visibility and parent relations.
- `JeeslFacade` declares generic methods on those markers, for example `<T extends EjbSaveable> T save(T o)`.
- The facade implementation is `AbstractJeeslFacadeBean` in `org.jeesl.controller.facade.jk` and its variant `jx`.
- The implementation runs the JPA operations on the `EntityManager` and carries `@TransactionAttribute`.
- `jeesl-api` adds one facade interface per feature, for example `JeeslIoMavenFacade`.
- `JeeslEap71FacadeLookup` implements `JeeslFacadeLookup` and reaches a facade over JNDI.
- The lookup takes an application name, a module name and connection settings.
- The package `org.jeesl.exception.ejb` holds `JeeslNotFoundException` and `JeeslConstraintViolationException`.
- The package `org.jeesl.controller.handler` holds lazy models, tuple handlers and REST handlers.
- The package `org.jeesl.controller.converter` holds the JSF converters of the features.
- The package `org.jeesl.controller.web` holds the web controllers that the views call.
- The package `org.jeesl.factory` builds entities and their XML, JSON and text representations.
- Entities live in `org.jeesl.model.ejb` and JSON objects in `org.jeesl.model.json`.
- The generated XML objects live in `org.jeesl.model.xml`.
- The XHTML views are packaged under `src/main/resources/META-INF/resources` in the name of their module.
- A feature module names its migration root and baselines through a `JeeslFlywayPathProvider`.

## Flow

Build:

1. The reactor reads the module list from the aggregator POM and orders the modules by their dependencies.
2. `jeesl-xml` generates the JAXB classes from the XSD in the `javax` and the `jakarta` variant.
3. Each module compiles against the generated model, and Surefire runs its JUnit tests.
4. The `release` profile attaches sources and javadoc and signs the artifacts.

Request:

1. A JSF view calls a web controller from `org.jeesl.controller.web`.
2. The controller reads and writes entities through a facade interface of `jeesl-api`.
3. The facade implementation performs the JPA operations and maps them onto the exceptions of `org.jeesl.exception.ejb`.
4. A REST endpoint is served by a handler from `org.jeesl.controller.handler.rest` on the same facade.
5. A separately running application reaches the facade through `JeeslFacadeLookup`.

Operation:

- Datasource and Hibernate settings are applied in the container.
- `jeesl-maven` provides templates for the application servers.
- Schema changes are delivered as the ordered SQL scripts of the feature modules.

## Technology

- Language: Java.
- Platform: Java EE with the `javax` namespace, alongside a `jakarta` variant of the generated XML model.
- Container: JBoss EAP.
- Persistence: JPA with Hibernate, EJB, and JDBC access through the helper classes of `jeesl-util`.
- Build: Apache Maven with a parent POM and imported BOMs.
- Web: JSF, JAX-RS over RESTEasy, and servlets.
- Serialization: JAXB for XML, Jackson and JSON-B for JSON.
- Logging: SLF4J, with log4j configurations in the test resources.
- Test: JUnit with the Surefire plugin.
- Documents and reports: Apache POI, Aspose Words, JasperReports and Apache Batik.
- Supporting libraries: EXLP, OpenFuXML and MetaChart.

The versions of the platform, the language, the frameworks and the build environment are maintained in the
authoritative location named below.

## Authoritative Locations

- Platform, language, frameworks, build environment, and pinned versions: **constraint**
  (`doc/requirements/constraints/`) or **decision** (`doc/decisions/`).
- Build of the project: **constraint** (`doc/requirements/constraints/`).
- Verification of the project: **constraint** (`doc/requirements/constraints/`).
- Selecting a technology, a protocol, or a storage format: **decision** (`doc/decisions/`).
- Behavior and values: the responsible **requirement** (`doc/requirements/`).
