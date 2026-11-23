---
id: FR-002
title: Methods to retrieve Security Objects
type: functional
status: implemented
priority: must
depends_on: []
related: []
---

# FR-002: Methods to retrieve Security Objects

## Requirement

`JeeslSecurityFacade.fSecurityActions(JeeslSecurityQuery)` returns each action that is effective for the users
and the roles of the query (AC-002-01).

- An action is effective when a role of a user of the query is assigned to the action (AC-002-01).
- An action is effective when a role of a user of the query is assigned to a usecase and the usecase is
  assigned to the action (AC-002-02).
- An action is effective when a role of a user of the query is assigned to the view of the action (AC-002-03).
- An action that no supported path reaches is not part of the result (AC-002-04).
- Several paths and several roles grant an action once (AC-002-05).
- Every role of the query is handled like a role of a user of the query (AC-002-06).
- The facade obtains the context roles through the query and reads no assignment of a consuming application
  (AC-002-06).
- A query with users and without roles selects through the roles of the users (AC-002-07).
- A query without users and without roles returns the actions that its filters allow (AC-002-10).
- The view of the query restricts the result (AC-002-08).
- The action code of the query restricts the result (AC-002-09).
- The action of the query restricts the result (AC-002-11).

## Rationale

- A security handler of a consuming application needs the actions of a view for the roles of a user and for
  the roles that a business context contributes.
- A single database-side selection keeps the assignment paths in one place instead of in every consumer.

## Scope

In scope:

- The effective action selection of `fSecurityActions` for the users and the roles of the query.
- The assignment paths role to action, role to usecase to action and role to view to action.
- The duplicate-free result and the existing filters of the query.

Out of scope:

- Project, cost unit, location and company assignments of a consuming application.
- The staff relation, the role lookup of a domain and the resolution of context roles.
- Ordering, pagination and the return type of the method.
- The facade variant in the `jakarta` namespace, whose methods are not implemented.

## Assumptions

1. The selection is one criteria query on the action entity with `EXISTS` predicates.
2. The role, usecase and action graphs are not loaded and filtered in Java.
3. The context roles reach the facade through `JeeslSecurityQuery.getSecurityRole()`.
4. The view, category, code and visibility filters of `pAction(...)` are retained.
5. The result keeps the return type `List<A>`, the pagination and the ordering rule of the method.
6. The verification is `EffectiveSecurityActionsIT` on an in-memory database.
7. The verification runs explicitly and is no test program of ADR-0003.
8. The action of the query is a filter of `pAction(...)`.

## Open Questions

None.

## Decided Questions

None.

## Acceptance Criteria

### AC-002-01: A role of the user is assigned to the action

Given:

- a user with the role `r1`
- the role `r1` is assigned to the action `a1`

When:

- the facade selects the actions with a query that carries the user

Then:

- the result contains `a1`

### AC-002-02: A role of the user reaches the action through a usecase

Given:

- a user with the role `r1`
- the role `r1` is assigned to the usecase `u1`
- the usecase `u1` is assigned to the action `a1`

When:

- the facade selects the actions with a query that carries the user

Then:

- the result contains `a1`

### AC-002-03: A role of the user is assigned to the view of the action

Given:

- a user with the role `r1`
- the role `r1` is assigned to the view `v1`
- the action `a1` belongs to the view `v1`
- no role is assigned to the action `a1`

When:

- the facade selects the actions with a query that carries the user

Then:

- the result contains `a1`

### AC-002-04: An action without a supported path is absent

Given:

- a user with the role `r1`
- the action `a2` is assigned to no role of the user
- the action `a2` belongs to no view of the role `r1`
- the action `a2` is assigned to no usecase of the role `r1`

When:

- the facade selects the actions with a query that carries the user

Then:

- the result contains no `a2`

### AC-002-05: Several paths grant the action once

Given:

- a user with the roles `r1` and `r2`
- the role `r1` is assigned to the action `a1`
- the role `r2` reaches the action `a1` through a usecase

When:

- the facade selects the actions with a query that carries the user

Then:

- the result contains `a1` exactly once

### AC-002-06: The roles of the query contribute the assignment paths

Given:

- the query carries the role `r1` and no user
- the role `r1` is assigned to the action `a1`
- the role `r1` is assigned to the usecase `u1` and the usecase `u1` is assigned to the action `a2`

When:

- the facade selects the actions with this query

Then:

- the result contains `a1`
- the result contains `a2`

### AC-002-07: An absent role list adds no path

Given:

- a user with the role `r1`
- the role `r1` is assigned to the action `a1`
- the action `a2` is assigned to no role of the user

When:

- the facade selects the actions with a query that carries the user and no role

Then:

- the result contains `a1`
- the result contains no `a2`

### AC-002-08: The view filter remains effective

Given:

- a user with the role `r1`
- the role `r1` is assigned to the actions `a1` and `a2`
- `a1` belongs to the view `v1` and `a2` belongs to the view `v2`

When:

- the facade selects the actions with a query that carries the user and the view `v1`

Then:

- the result contains `a1`
- the result contains no `a2`

### AC-002-09: The action code filter remains effective

Given:

- a user with the role `r1`
- the role `r1` is assigned to the actions `a1` and `a2`

When:

- the facade selects the actions with a query that carries the user and the action code of `a1`

Then:

- the result contains `a1`
- the result contains no `a2`

### AC-002-10: A query without users and without roles applies the filters only

Given:

- the query carries no user and no role
- the action `a1` belongs to the view `v1`
- the action `a2` belongs to the view `v2`

When:

- the facade selects the actions with the view `v1`

Then:

- the result contains `a1`
- the result contains no `a2`

### AC-002-11: The action of the query restricts the result

Given:

- a user with the role `r1`
- the role `r1` is assigned to the actions `a1` and `a2`
- the query carries the user and the action `a1`

When:

- the facade selects the actions with a maximum of five results

Then:

- the result contains one action
- the action is `a1`

## Dependencies

None.

## Evidence

### Implementation

- `ejb/src/main/java/org/jeesl/controller/facade/jx/system/JeeslSecurityFacadeBean.java` – `fSecurityActions(...)`,
  `pEffective(...)`, the six path predicates and the action filter select the effective actions
  (AC-002-01, AC-002-02, AC-002-03, AC-002-04, AC-002-05, AC-002-06, AC-002-07, AC-002-08, AC-002-09,
  AC-002-10, AC-002-11)
- `system-security/src/test/java/org/jeesl/model/ejb/system/security/user/SecurityTestUser.java` – the roles of
  a user for the verification (AC-002-01)

### Tests

- `mvn -o -pl system-security test -Dtest=EffectiveSecurityActionsIT` – 11 tests, 0 failures
  (AC-002-01 to AC-002-11)
