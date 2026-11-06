# AGENTS.md — Binding Instructions for Codex / AI Coding Agents

**Scope:** Entire repository; any nested `AGENTS.md` is permitted only if it adds stricter, non-conflicting local rules. Read this file, `README.md`, relevant `docs/*`, and actual build files **before editing code**. Our project is **one custom NeoForge RPG mod** plus an independently authored handcrafted Minecraft world and optional launcher-managed modpack (with a dedicated server implementation eventually planned); never reinterpret the repository as multiple gameplay mods.

## 0. First-step environment check

1. Run `pwd`, inspect the repository root and `git status --short`; **do not assume** that any README tree entry exists.
2. Check for `gradlew`, Gradle files, Java sources, exact Minecraft version, NeoForge patch, Java toolchain and mod ID. No source/build files present = planning repository, **not** a running mod.
3. Read `UPDATES.md`, `docs/SKILL_TREES.md` (full draft), `docs/GAME_DESIGN.md`, `docs/MOD_COMPATIBILITY_RESEARCH.md`, `docs/MANUAL_WORLD_PIPELINE.md`, `docs/INSTALLER_AND_ENV.md` and `docs/LEGAL_AND_RELEASE.md` for affected work.
4. Read the latest pertinent ADR before altering a decided behavior. Prefer direct product requirements over unverified assistant proposals; preserve unknowns as unknowns.
5. Don't use dynamic/latest dependency versions, fabricated release IDs, guessed mod metadata or outdated Minecraft package paths. The **suggested** baseline is Minecraft **1.21.1 / NeoForge 21.1.x / Java 21**, not yet a pinned reproducible build.

## 1. Non-negotiable product invariants

- A **single built JAR** contains original RPG gameplay: classes, state, combat, skills, economy, story, Ether Tears, map fog, quest authority and boss mechanics. External mods are libraries or external content, **not submodules or copied code**.
- **No procedural campaign map**. The world and Act II corrupted counterpart are manually built and versioned separately; never generate terrain, structures, regions or coordinates by assumption.
- Classes by **armor**: Warrior, Mage, Archer, Priest. **Full set** only: class passive, unique set effect, **+50% relevant-class-damage multiplier applied once**. Final mixed-armor rule unresolved—ADR before implementation.
- Eight subclasses: Warrior Duelist/Juggernaut; Mage Battlemage/Arcanist; Archer Marksman/Skirmisher; Priest Hierophant/Inquisitor. ONE active subclass for a class build. Weapons gate subclass effects but equipping a weapon never selects an unpurchased subclass.
- Skill design `docs/SKILL_TREES.md`: 128 nodes total (32/class: 12 shared+10/branch×2); level cap 50; 49 SP from levels 2–50; shared tree 24 SP + one branch 40 SP = **64 SP**, so no full completion. **No extra farmable or exploitable SP**. Level-45 Ascension options are mutually exclusive. Level gates: subclass 25, passive 27, active 30, advanced 35, endgame 40, Ascension 45. One equipped subclass passive and one active, four choices per subclass in each category, choices cost 0 SP.
- Prevent armor swaps, death, loadout changes, re-log, dual wield, weapon tags, status proc recursion or cross-mod damage hooks from bypassing costs/cooldowns/unlocks. Gold and one-time item rewards must be atomic/idempotent.
- Server computes all damage, unlocks, purchases, quest flags, teleport permissions and rewards; client only displays/requests. Test multiplayer, death, dimension changes, restart, data migration. **Dedicated server implementation is eventually planned:** keep headless dedicated server compatibility at the forefront of all design and code; zero client-class references in common/server logic, no singleplayer-only assumptions, and full multiplayer state isolation.
- Distinguish confirmed design, previous **balancing draft**, research, accepted ADR, implemented feature, passing automated test and validated in-game behavior. Do not promote a draft into product canon silently.

## 2. Source-of-truth precedence

**Platform APIs:** pinned project's actual `gradle.properties` and resolved dependency versions plus official version-specific NeoForge/API docs. **Design:** approved `docs/decisions/` ADRs, then approved full design documents (most notably `docs/SKILL_TREES.md`), then validated runtime JSON. README is a navigation/architecture guide, **not** a substitute for detailed abilities. If code and design disagree, do **not** silently accept code as correct—flag discrepancy and test/migrate thoughtfully.

No version or license claims in `docs/MOD_COMPATIBILITY_RESEARCH.md` mean that all mods have been run together; those claims are **published-file verification only** until logged. Do not claim open source or redistribute rights from mere access to GitHub source.

## 3. Directory rules

- `src/main/java/<base_package>/` = exactly one mod. Small, focused packages: `registry`, `player`, `progression`, `classsystem`, `skills`, `abilities`, `stats`, `combat`, `magic`, `equipment`, `items`, `economy`, `story`, `travel`, `map`, `encounters`, `bosses`, `world`, `networking`, `commands`, `client`, `compat`.
- `src/main/resources/assets/<modid>/` = our licensed client assets; `src/main/resources/data/<modid>/` = proper 1.21.1 datapack structures and custom data with actual loaders. No accidental third-party content.
- `modpack/` = configs, launcher manifests, optional pack scripts, datapacks/resourcepacks; **not an independent Java mod**. Never commit downloaded dependency JARs unless their specific redistribution rights and intended platform packaging have been approved/documented.
- `world/` = original hand-authored map manifests/schematics/exports. Keep working copies, backups, `session.lock`, playerdata, caches, secrets and massive temporary regions out of Git. Use release artifacts or Git LFS for deliberately selected map exports.
- `docs/` = requirements, researched evidence, version matrix, ADRs and test procedures. Do not create hundreds of empty stubs simply to mirror the proposed tree.

## 4. Third-party mod integration rules

**Gate A — eligibility:** Verify exact `1.21.1 + NeoForge` file, official host, project and file ID, dependency graph, environment (client/server), license including *assets*, platform-specific pack inclusion permission, and distribution method. An official public listing is **not** blanket permission to ship a mod JAR. Pin in `modpack/manifests/` **only after** confirming selected versions. Read legal policy before adding to release manifests.

**Gate B — proof-of-concept:** Each integration starts from a small throwaway sandbox or isolated branch. At minimum build + client + dedicated server startup; success/failure logs; update `docs/COMPATIBILITY_MATRIX.md` with observed findings. Runtime availability is *not* proven by website labels.

**Gate C — clean architecture:** Wrap APIs in `compat/<modname>/` adapters; do not make our stat/combat/quest models depend directly on third-party implementation internals. NeoForge mod-presence check is not sufficient if JVM eagerly classloads an absent integration class—use separate loading boundaries. Use explicit compile-only/runtime dependency definitions from author docs; do not shade/relocate someone else's JAR into ours.

**Required ownership choices:**
- **Combat:** Vanilla/custom engine OR Better Combat OR Epic Fight; **not both combat overhauls**. Test Better Combat + Player Animator + Cloth Config as a group if piloted. Combat Roll is another separate risk for dodge, i-frame and stamina overlap; adopt only by explicit ADR.
- **Travel:** Our Ether Tear, Pocket Hub and map reveal rules supersede Waystones. Leave Waystones out unless its teleports are strictly constrained and documented.
- **Quest progression:** Our story service is canonical. FTB Quests may present/track commissions and journals; rewards go through idempotent server transactions. FTB XMod Compat **21.1.14** requires FTB Library **≥2101.1.38**—do not assemble mismatched FTB versions.
- **NPCs:** Easy NPC's bundle is a meta-package; install matching **Core + Config UI** modules if used. Its MIT code license **does not cover assets**. NPC UI callbacks never grant Gold/flags without our server validation.
- **Recipes:** Choose JEI **or** EMI. KubeJS should be reserved for pack config, not core RPG class/skill/quest logic.
- **Map:** WorldEdit builder profile only; Axiom is Fabric-only and subject to a separate EULA/commercial license. Never ship a Fabric-only builder tool inside the NeoForge runtime pack.
- **Enemies/loot:** Lootr only for ordinary per-player loot; story-specific treasure remains our transaction/state system. Mowzie's/Cataclysm custom licenses forbid assuming permission to extract/modify models/sounds/AI. Use external mobs intact and linked by tags/config, if adopted.
- **Magic:** Iron's Spells and Ars Nouveau are deliberately deferred; do not import their independent leveling/spell economies before a design ADR.

## 5. Code quality and implementation discipline

- Use **immutable stat snapshots** or deterministic pure stat functions. One authoritative modifier bucket strategy; one source for +50% set buff. Test caps, stacking, procs and boss resistance from `docs/SKILL_TREES.md`.
- Persist stable, namespaced IDs for classes, skills, equipment, map regions, NPCs, quest flags and Ether Tears. Data migrations must maintain legacy IDs and avoid item loss.
- Use NeoForge events/registers/packets matching the pinned version; do not import client renderer classes on a dedicated server; no client-trusted damage or inventory state.
- Content JSON: schema, validation, duplicate/missing reference checks, cycle/unreachable-node checks, stable IDs, safe reload and sync. Don't pretend a custom `data/<modid>/skill_trees/` directory works without a loader.
- No arbitrary Mixin into third-party mod internals unless official APIs are insufficient, there is a documented ADR, license review, compatibility tests and narrow version guards.
- Use localization keys for UI, avoid tick-wide expensive scans, cache carefully without cross-player leaks, never suppress errors with broad catches or fake defaults.
- No placeholder Java mechanics with claims such as “implemented”. If design is not settled, write a tiny ADR or document the blocker and implement unrelated safe work.

## 6. Manual map safety rules

- Treat the author's editable world save as irreplaceable production data. **Never edit original without a verified backup.** No auto-moving/renaming world folders or loading it under changed mod lists without rollback path.
- The game has **two hand-authored map states**: Act I physical world and Act II corrupted counterpart. Coordinate references and inter-world portals are explicit, not guessed or generated. Architect map mirroring in a chosen dimension/save topology ADR.
- Commit region/POI **identifiers** first; derive coordinates *only* from the actual world/schematic and author-provided data. No invented spawns, quest NPC positions, teleport coordinates, chest locations or borders.
- Preserve scripted flags across dimension switch; Ether Tears reset **except the Hub** at Act II transition; ensure one-time transition cannot be farmed.
- Keep build-only WorldEdit operations on staging worlds; region and block changes can destroy player progression, loot and NPCs. Validate world checksums / backups before release.
- World build scripts may make **reproducible, opt-in modifications** to a test copy only, never run automatically on a player's world.

## 6A. Windows installer and `.env` contract (binding)

- **Canonical variables**, defined in [`.env.example`](.env.example): `MINECRAFT_DIR` (read-only discovery hint), `MINECRAFT_INSTANCE_DIR` (**selected launcher game root**, not necessarily vanilla), `RPG_INSTALLER_STAGING_DIR` (safe app-owned staging), `RPG_PACK_FILE`, `RPG_WORLD_ARCHIVE`, `RPG_LAUNCHER`, `RPG_MODPACK_VERSION`.
- Keep a local **`.env` ignored by Git**. Expand Windows `%APPDATA%`/`%LOCALAPPDATA%` on **the target player's system**. Never substitute the developer's absolute path into release assets or install a modpack into guessed `%APPDATA%\.minecraft`.
- The recommended installer uses **authorized launcher imports** of `.mrpack`/CurseForge manifest; do not write your own unapproved third-party mod JAR downloader or bundle Minecraft executable/JARs, game assets, tokens or unsupported launcher APIs. See `docs/INSTALLER_AND_ENV.md` and `installer/README.txt`.
- `installer/setup.iss` is a staging EXE blueprint; `installer/build_installer.ps1` reads `.env` to validate release payloads; `installer/prepare_instance.ps1` is an **opt-in** save import helper. None is a tested end-to-end one-click installer. Do not claim it is.
- An instance path must be confirmed and validated (`mods/`, world root, write permission, canonical path) before copying content. No silent overwrites of saves, block mods, mods, configs, `options.txt`, accounts or EULA prompts. All ZIP entries must be normalized, path-contained and checked against zip-slip/drive/symlink/excessive-size attacks.
- Store original map/world saves outside the EXE and repository until deliberately exported and licensed. Install world as a new unique save; uninstall or upgrades **must not delete saves**, playerdata, mods or shared launcher profiles by default.
- Installer acceptance testing: clean Windows VM/user, selected custom instance, no Minecraft installed, missing launcher, relocated launcher, path with spaces/unicode, already existing save, malformed zip, no permission, integrity check, uninstall/upgrade and repeated run. Use actual logs.
- **Build artifact guard:** no compiled EXE, approved `.mrpack`, world ZIP or game launcher has been produced in a docs-only seed. `-ValidateOnly` cannot be counted as installer QA.

## 7. Required checks / commands

1. Before editing: `git status --short` and inspect actual repo, config and relevant docs.
2. Unit tests: point scarcity, valid paths, exclusive nodes, class assignment, modifier stacking, boss damage scaling, one-time rewards, transaction rollback, fog/torn travel flags.
3. If MDK exists: `./gradlew test` and `./gradlew build` (or `gradlew.bat` on Windows). Run official dev client/server tasks from the **actual** Gradle setup; don't guess task names.
4. Integration: vanilla/custom baseline first; then toggle **one dependency group at a time**; test client render, headless dedicated server, multiplayer, reload, relog, death, map loading and upgrade path. Test optional-missing-mod cases if declared optional.
5. CI: never mark successful builds/tests unless there are logs; report not-run with reason. Avoid `./gradlew ...` on a docs-only repository and then claiming test coverage.
6. For world changes: verify an isolated copied save, dimension/POI references, protection boundaries, backups and multiplayer teleport cases.
7. For installer changes: validate `.env` format, Inno Setup source paths, approved manifests, staging behavior, and safe world-import logic; do not claim pass without Windows execution.

## 8. Mandatory output of every agent task

In the final task summary state: **(a)** files changed, **(b)** important assumptions vs confirmed sources, **(c)** any dependencies added with pinned IDs/license and official URLs, **(d)** tests run plus actual pass/fail output, **(e)** multiplayer/world impact, **(f)** unresolved issues and next vertical slice. No invented screenshots, source commits, external permissions, compiled mods or in-game test results.

**Repository log policy:** Keep [`README.md`](README.md) concise, high-level, and clean. Whenever substantive milestones, architecture decisions, or vertical slices are completed, record elaborate, detailed updates in [`UPDATES.md`](UPDATES.md) rather than expanding `README.md`.

## 9. Forbidden shortcuts

- Do not download from third-party JAR mirrors, bypass platform distribution policies, scrape or paste proprietary source/assets, or remove credits/licenses.
- Do not claim ARR means “illegal in modpack”; similarly do not claim that Modrinth/CurseForge availability permits standalone JAR redistribution.
- Do not remove the intended scarcity of points, bypass narrative unlocking, substitute travel/fog systems with unrelated mod features, or abandon the manually authored world.
- Do not combine Better Combat + Epic Fight, or renderers with overlapping patches, on the assumption that they coexist.
- Do not change user-authored story/build assets or treat unapproved design draft numbers as tested balance outcomes.

**First implementation request to execute after platform lock:** import matching NeoForge MDK, create a minimal one-mod package, then implement the tiniest executable Warrior armor/damage/skill state vertical slice with pure logic tests; independently test Easy NPC and combat-engine choices before adopting them. Do not scatter premature empty classes throughout the planned tree.
