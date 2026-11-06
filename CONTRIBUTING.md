# Contributing

Read [AGENTS.md](AGENTS.md), [the team map](docs/development/TEAM_WORK.md) and affected design docs first. Check actual build files and `git status --short` before editing. No runnable mod exists yet.

1. Open or claim a scoped issue with an owner, reviewer, touched paths, dependencies and acceptance criteria.
2. Use a short feature branch such as `feature/warrior-state`; keep unrelated formatting and generated files out of the change.
3. Agree on shared IDs/contracts before parallel edits. Record consequential choices in `docs/decisions/`.
4. Implement a small end-to-end slice with relevant tests and assets. Keep client-only classes inside `client/` and server authority in common gameplay services.
5. Report actual checks and evidence in the PR, including not-run checks. Once the MDK exists, use only its real build/test/dev tasks.
6. Review persistence/networking changes with the platform owner and cross-feature changes with the affected owner. Log substantive milestones in `UPDATES.md`.

Never commit credentials, local game instances, downloaded dependency JARs, live saves or unreviewed large exports. The root ignore rules reduce accidental staging; still inspect the staged diff.
