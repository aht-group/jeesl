# Rules for Git Commit Messages

**The AI assistant does not commit.** It creates no commits (`git commit`, `git commit --amend`, `git cherry-pick`)
and no tags, and it runs no history-changing command such as `git reset` on commits. It leaves every change in the
working tree and reports it; staging, committing, and rewriting history are the decision of the responsible person
alone. The assistant commits only when that person asks for a commit explicitly in the current task.

- Conventional Commits: type, then the requirement ID as the scope if a reference exists.
- Allowed types: `docs`, `feat`, `fix`, `refactor`, `test`, `build`, `chore`.
- Subject in imperative English, at most 72 characters, no trailing period.
- Examples: `docs(FR-001): clarify output after reset`, `feat(NFR-003): add value holder`,
  `docs: deprecate FR-001`.
- A commit contains one delimited change; implementation and requirement change are not mixed
  if they are independent of each other.
- The change log (`doc/changelog.md`) names source artefacts by their simple class name only
  (`RestPaginationFilter`); the `Files:` line carries no repository path.

