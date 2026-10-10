---
id: FR-001
title: Reusable help, logging and configuration handling for Apache Commons CLI entry points
type: functional
status: approved
priority: must
depends_on: []
related: []
---

# FR-001: Reusable help, logging and configuration handling for Apache Commons CLI entry points

## Requirement

`JeeslCliOptionHandler` provides the help output, the logging initialisation and the configuration
selection for command-line entry points that parse their arguments with Apache Commons CLI (AC-001-01).

- Every entry point registers the `help` option and the `debug` option through
  `JeeslCliOptionHandler.buildHelp()` and `JeeslCliOptionHandler.buildDebug()` (AC-001-01).
- Every entry point handles help and logging through `JeeslCliOptionHandler.handleHelp(CommandLine)`
  and `JeeslCliOptionHandler.handleLog4j2(CommandLine)` (AC-001-02, AC-001-03).
- Every entry point prints the help text and ends the process with exit code `0` when the `help`
  option is present (AC-001-02).
- Every entry point prints the help text and ends the process with exit code `0` when the argument
  parser reports a parse error (AC-001-06).
- The mandatory options are `help` and `debug`; the `logFile` option and the `config` option are
  optional (AC-001-01).
- Every entry point initialises logging through the handler and selects the profile from the
  `logFile` option, the `debug` option or the default (AC-001-03, AC-001-04, AC-001-05).
- Every entry point registers the `config` option through `JeeslCliOptionHandler.buildConfig()`
  (AC-001-09).
- Every entry point reads its configuration through the handler when the `config` option is present
  (AC-001-10).
- The value `exlp` of the `config` option selects the central configuration (AC-001-11).
- An unavailable configuration file ends the process with a non-zero exit code (AC-001-12).
- The handler is usable from another module of the repository and from a consuming application
  (AC-001-07).
- At least one class of the repository outside the handler's module uses the handler (AC-001-08).

## Rationale

- One handler keeps the option names, the help output, the logging bootstrap and the configuration
  selection of the consuming entry points in agreement.
- A consuming application reuses the handler instead of repeating the help, logging and configuration
  code.

## Scope

In scope:

- The handler as the reusable entry point for applications that parse arguments with Apache Commons CLI.
- The `help` option, the help output and the logging initialisation.
- The `config` option and the configuration selection.
- The class of the repository that uses the handler.

Out of scope:

- The command options that an entry point defines in addition to `help`, `debug`, `logFile` and
  `config`.
- The content of the configuration files.

## Assumptions

1. The handler is `org.jeesl.controller.handler.cli.JeeslCliOptionHandler`.
2. The help option is named `help`.
3. The debug option is named `debug`.
4. The log file option is named `logFile`.
5. The configuration option is named `config`.
6. The help text is rendered by `org.apache.commons.cli.HelpFormatter`.
7. The profiles are `app.log4j2.xml` (default), `debug.log4j2.xml` and `file.log4j2.xml`.
8. The argument parser is `org.apache.commons.cli.DefaultParser`.
9. Logging is bootstrapped by `org.exlp.controller.handler.io.log.LoggerBootstrap`.
10. The entry points read the configuration through `JeeslCliOptionHandler.config2Wrapper(CommandLine, String)`.

## Open Questions

None.

## Decided Questions

1. **Scope of the reuse**
   Question: For which applications does the requirement provide the entry point?
   Decision: For consuming applications outside the repository; within the repository at least one class uses it.
   Applies in: ## Requirement
2. **Exit on help**
   Question: With which exit code does the process end when the `help` option is present?
   Decision: With exit code `0`.
   Applies in: AC-001-02
3. **Parse error**
   Question: What does an entry point do when the argument parser reports a parse error?
   Decision: It prints the help text and ends the process with exit code `0`.
   Applies in: AC-001-06
4. **Option set**
   Question: Must every entry point register all options, or only the options that it uses?
   Decision: `help` and `debug` are mandatory; `logFile` and `config` are optional.
   Applies in: ## Requirement
5. **Central handling call**
   Question: Does an entry point call one handler method for parsing and handling, or the individual steps?
   Decision: The individual steps.
   Applies in: ## Requirement
6. **Status**
   Question: Is the requirement a record of the existing implementation or a new requirement?
   Decision: A new requirement.
   Applies in: the index file
7. **Priority**
   Question: Which priority does the requirement carry?
   Decision: `must`.
   Applies in: the index file
8. **Configuration option**
   Question: Is the `config` option of the handler part of this requirement, or out of scope?
   Decision: It is part of the requirement; the entry points handle it identically.
   Applies in: ## Requirement

## Acceptance Criteria

### AC-001-01: The option set carries help and debug

Given:

- a command-line entry point that parses its arguments with Apache Commons CLI

When:

- the entry point builds its options

Then:

- the option set contains an option named `help`
- the option set contains an option named `debug`

### AC-001-02: Help ends the process

Given:

- a command line that carries the `help` option

When:

- the entry point handles the command line

Then:

- the help text is printed
- the process ends with exit code `0`

### AC-001-03: Debug selects the debug profile

Given:

- a command line that carries the `debug` option and no `logFile` option

When:

- the entry point initialises logging

Then:

- logging is bootstrapped from `debug.log4j2.xml` (Assumption 6)

### AC-001-04: Default profile

Given:

- a command line that carries neither the `debug` option nor the `logFile` option

When:

- the entry point initialises logging

Then:

- logging is bootstrapped from `app.log4j2.xml` (Assumption 6)

### AC-001-05: Log file selects the file profile

Given:

- a command line that carries the `logFile` option

When:

- the entry point initialises logging

Then:

- logging is bootstrapped from `file.log4j2.xml` (Assumption 6)

### AC-001-06: A parse error ends the process

Given:

- a command line that the argument parser rejects

When:

- the entry point handles the parser result

Then:

- the help text is printed
- the process ends with exit code `0`

### AC-001-07: The handler is usable from another module

Given:

- a module that depends on the artifact of the handler's module

When:

- the module builds its options through a new `JeeslCliOptionHandler`

Then:

- the call compiles
- the option set contains an option named `help`

### AC-001-08: A class of the repository uses the handler

Given:

- the source tree of the repository outside the handler's module

When:

- the source tree is inspected

Then:

- at least one class references `JeeslCliOptionHandler`

### AC-001-09: The option set carries config

Given:

- a command-line entry point that parses its arguments with Apache Commons CLI

When:

- the entry point builds its options

Then:

- the option set contains an option named `config`

### AC-001-10: The config option selects the configuration file

Given:

- a command line that carries the `config` option with the value of a configuration file

When:

- the entry point reads its configuration

Then:

- the configuration of that file is loaded

### AC-001-11: The value exlp selects the central configuration

Given:

- a command line that carries the `config` option with the value `exlp`

When:

- the entry point reads its configuration

Then:

- the central configuration is selected

### AC-001-12: A missing configuration file ends the process

Given:

- a command line that carries the `config` option with the value of an unavailable file

When:

- the entry point reads its configuration

Then:

- an error is logged
- the process ends with a non-zero exit code

## Dependencies

None.

## Evidence

### Implementation

- `util/src/main/java/org/jeesl/controller/handler/cli/JeeslCliOptionHandler.java` – the help, logging and
  configuration methods (AC-001-01, AC-001-02, AC-001-03, AC-001-09)
- `client/src/main/java/org/jeesl/client/app/JeeslMailSpooler.java` – `parseArguments(...)` and `main` use
  the handler (AC-001-01, AC-001-02, AC-001-06, AC-001-08)
- `client/src/main/java/org/jeesl/client/web/rest/JeeslDbBackupNotifier.java` – `parseArguments(...)` and
  `main` use the handler (AC-001-01, AC-001-02, AC-001-06, AC-001-08)
- `client/src/main/java/org/jeesl/client/web/rest/JeeslFontTrackerApp.java` – `parseArguments(...)` and
  `main` use the handler (AC-001-01, AC-001-02, AC-001-06, AC-001-08)
- `util/src/main/java/org/jeesl/controller/io/mail/AbstractSmtpSpooler.java` – `createOptions()` builds
  `help` and `debug` through the handler (AC-001-01)
- `client/pom.xml` – depends on `jeesl-util` (AC-001-07)
- `client/src/main/java/org/jeesl/client` – open: the entry points register the `config` option and read
  the configuration through the handler (AC-001-10, AC-001-11, AC-001-12)

### Tests

- open: an automated test covers the handler and the entry points (AC-001-01 to AC-001-12)

### Documentation

- open: the architecture file names the handler as the reusable entry point (AC-001-01)
