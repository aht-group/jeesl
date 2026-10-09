# Rules for Shell Commands (Search Space and Runtime)

**In short:** Never search from the **file-system root**, from the **home or user directory**, or from a
**system directory** – on macOS and Linux, therefore, **not** from `/`, `~`, `$HOME`, `/Users`, `/home`,
`/System`, `/Library`, `/Applications`, `/Volumes`, `/private`, `/opt`; on Windows **not** from `C:\`,
`%USERPROFILE%`, `C:\Users`, `C:\Windows`, `C:\Program Files` or a drive root such as `D:\`. Search **only**
within the project and within **documented** paths whose location follows from a file of the repository.
Known paths are **derived**, not searched. Every recursive command (`find`, `grep -r`, `ls -R`) carries a
**depth limit** and a **time limit**.

Basis: an instruction of the responsible person, after a run from the machine root (`find / …`) had been
aborted repeatedly. The reference to this rule appears in the **project-independent** instruction
`docs/llm.md` (section “Workflow” and section “Documentation”); the names of the AI tool's rule files and
their location are given in the **Project Overview**.

## The Rule

### 1. Search Space

Permitted as a search root are

- the **repository root** with its subdirectories; the **Project Overview** names the project's location and
  leading subdirectories,
- the project's **build folder**; the **build configuration** names its location and name, and the build
  tools name their paths themselves (Section 3),
- the **program directory** of the build tool used, as far as its location follows from a file of the
  repository and its size remains limited,
- **not** the unversioned working directories – the **staging repository** and the **working repository**;
  separate rules apply to them in the AI tool's rule file on the working directories (its name is given in
  the **Project Overview**).

Not permitted as a search root are the **file-system root**, the **home or user directory**, and the
operating system's **system directories** – on macOS and Linux, for example, `/`, `~`, `$HOME`, `/Users`,
`/home`, `/System`, `/Library`, `/Applications`, `/Volumes`, `/private`, `/opt`; on Windows, for example,
`C:\`, `%USERPROFILE%`, `C:\Users`, `C:\Windows`, `C:\Program Files` and every drive root (`D:\`). A run
from the machine root is **inadmissible even when** only a single file name is known.

### 2. Tool First

Within the project, the AI assistant's search tools are used first (file and text search in the workspace);
they know the project's exclusions (such as the version-control directory `.git`, the build folder, and the
unversioned working directories). `find`, `grep -r` and `ls -R` in the shell remain the **exception**. That
a tool **shows** an exclusion is **not** permission to read it: the **staging repository** is **not** read,
and the **working repository** is used **only** on explicit request (the AI tool's rule file on the working
directories; its name is given in the **Project Overview**).

### 3. Deriving Known Paths Instead of Searching

A path that follows from a file of the repository is **formed**, not searched:

- **Build results** always lie in the project's **build folder**; the **build configuration** names its
  location and name. Both are details of the individual project and appear in the **Project Overview**,
  **not** here. Instead of searching, the path is **formed** or the folder is **listed**
  (`ls -l <build folder>/`).
- **The build tools name their paths themselves:** a run with verbose output shows the paths in its command
  lines; a size or memory report and the build's **map file** lie next to the artifact.
- The **location and invocation of the build tool** follow from the build configuration or from the
  **Project Overview**; they are **formed**, not searched.

### 4. Obligations for Every Recursive Command

Every recursive command – even within the project – fulfils **all** four points:

- **A concrete root** instead of the machine root or the home directory.
- **Depth limit**: `find ... -maxdepth <n>` with `n` ≤ 4.
- **Time limit** **before** the command: on Linux `timeout 20`; on macOS `timeout` from the GNU tools
  (`coreutils`) or `gtimeout`; on Windows the equivalent of the shell used.
- **Suppress error output** (`2>/dev/null`) and exclude the version-control directory
  (`-not -path '*/.git/*'`).

This form applies to POSIX shells (macOS, Linux, Windows with Git Bash or WSL). In another shell (for
example, PowerShell), that shell's means for depth limit, time limit and error output take their place.

A permissible example (the root is documented here because it appears in Section 1):

    timeout 20 find <documented-path> -maxdepth 4 -name '<pattern>' -not -path '*/.git/*' 2>/dev/null

Where a **file-name index** of the system is available, it replaces the traversal – for example, on **macOS**
`mdfind -onlyin <folder> 'kMDItemFSName == "<name>"'` (Spotlight index). It too is to be limited to a folder;
for a traversal of one's own, the depth limit from this section remains in force.

### 5. Report Instead of Guessing

If a path is **not** derivable from the repository, it is **asked for** or determined via the build tools
(Section 3). There is **no** guessing followed by a broad search. If a result remains incomplete because a
limit from Section 4 was reached, this is **stated** in the result (“search limited to `<documented path>`
up to depth `<n>`”) – instead of removing the limit.

### 6. Long Runs Are Not a Search Problem

Commands that take time by their nature (build, flashing, test run) do **not** fall under this rule. For
them: run them in the background, wait for the output, do not abort.

## What Does Not Belong to This Rule

- **Not** the responsible person: the rule binds **exclusively** the AI assistant. By hand, commands
  without limits are permissible.
- **Not** the choice of the search root **within** the project: there the task decides, not this rule.
- **Not** the content of the task: this rule forbids no subject-matter command, but limits only its search
  space and its runtime.
- **Not** the details of the individual project: build folder, build tool, environment names and artifacts
  appear in the **Project Overview** and in the **build configuration**, **not** in this rule; it remains
  **project- and platform-independent**.
