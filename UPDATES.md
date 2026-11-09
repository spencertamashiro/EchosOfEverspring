# UPDATES.md — Project Activity & Changelog

This document tracks detailed progress logs, milestone summaries, and architectural evolution for `tentativeRPG`. High-level repository navigation and identity remain in [`README.md`](README.md) to keep its word count concise.

---

## Update Log
### [2026-11-09] Fix the RPG Series melee-skill crash

- Pinned Spell Engine 1.9.8 for Minecraft 1.21.1 on NeoForge. The release fixes [upstream issue #174](https://github.com/ZsoltMolnarrr/SpellEngine/issues/174), which caused a server crash when a melee skill broadcast effects without Better Combat installed.
- Clarified the roles of Spell Engine, Skill Tree (RPG Series), Pufferfish's Skills and Player Animator in the combined playtest guide.
- The isolated client joined a copied test world. The integrated server processed a Swipe attack-effects packet and stayed running. The Gradle build, unit tests and all three dedicated GameTests passed.
### [2026-11-08] Match skill connectors to tab themes

- Replaced the global gray connector with a solid color per tab. The four class tabs use their existing title accents and Weapon Skills uses cool steel blue. Connector geometry and purchase rules stay the same.
- Documented merge and push expectations in AGENTS.md.

### [2026-11-08] Integrate Everspring skill-tree playtest

- Folded the four Everspring class trees, Weapon Skills visual overlay, landscape assets, and client-only connector renderer into the single TentativeRPG JAR. The local standalone test project is no longer needed in the combined runtime.
- Added pinned, separately resolved RPG Series dependencies. See the [dependency lock](docs/development/skill-dependencies.lock.json) and [renderer decision](docs/decisions/0002-rpg-series-skill-playtest.md).
- Gradle build and two tooltip unit tests passed. All three dedicated GameTests passed, covering the NPC/quest integration and skill-tree purchase rules. See [features and playtest instructions](docs/development/COMBINED_PLAYTEST.md).


### [2026-11-08] Integrate NPC dialogue and item tooltips

- Added a single TentativeRPG Gradle build for Minecraft 1.21.1, NeoForge 21.1.265 and Java 21. The JAR combines the local RPGtestingNPC dialogue and FTB Quest prototype with the standalone Tooltip RPG renderer. It keeps the legacy NPC bindings, quest IDs, voice locations and item tooltip metadata.
- The client owns dialogue rendering, voices and animated item cards. The server validates dialogue choices and supply turn-ins. The prototype cast and chapters are development content. Original rose and thorn ornaments accompany Minecraft's container textures.
- The build, both tooltip unit tests and both dedicated-server NPC GameTests passed. The development client reached resource and audio initialization with Tooltip Overhaul 2.1.0. See [compatibility checks](docs/COMPATIBILITY_MATRIX.md) and the [integration guide](docs/development/PROTOTYPES.md).
- The next gameplay slice is the server-owned campaign quest flow. Live multiplayer and manual interaction with the combined client UI are pending.

### [2026-11-06] Focus Repository on Mod Development

- Removed empty scaffolding for a future modpack, installer, server deployment, separate campaign-world exports, and supporting tools. Preserved authored game and skill-tree design, original-art and mod source scaffolding, and compatibility research.
- Updated repository guidance and team workstreams to make the single NeoForge mod the current scope; future pack and world work is deferred. Client/server separation remains a mod requirement.
- No dependencies or gameplay changes. The repository still has no MDK or runnable build. Next: agree on platform and mod identity, then initialize the mod and implement a small tested vertical slice.


### [2026-11-06] Development Directory Scaffold and Team Boundaries

- Added one-mod source package reservations for all 22 concerns from AGENTS.md, runtime asset/data folders, pure-test and generated-resource reservations. No Java mechanics, loader metadata or Gradle tasks were fabricated.
- Added separate original-art, hand-authored Act I/II schematic, map manifest/export, launcher profile, dedicated-server operations, installer and tooling areas. Git-retained empty folders use `.gitkeep`; concern READMEs define responsibilities.
- Added `CONTRIBUTING.md`, a proposed workstream/contract handoff map, ADR template, testing/evidence guide, compatibility report index and PR template. No contributor assignments or architecture proposals were marked accepted.
- Added root ignore rules for secrets, game instances, builds, raw world saves, dependency JARs and release archives. Existing design and Obsidian content was preserved.
- `dev.tentativerpg` and `tentativerpg` are provisional filesystem names only. Exact platform versions, registry identity, package ownership, world topology and combat engine remain unresolved.
- Validation: directory/link checks and `git diff --check`; no build or in-game tests apply because no MDK or implementation exists. No dependencies added; no multiplayer state or world data changed.
- Next slice: approve platform/identity ADR, import matching NeoForge MDK, implement minimal Warrior server state/stat/unlock behavior with pure tests, then prove client and dedicated-server startup.


### [2026-11-06] Dedicated Server Architectural Scope
- **Commit:** `d4af592`
- **Scope & Details:**
  - Formally codified that an eventual dedicated server deployment is planned alongside the custom single-JAR mod, handcrafted campaign world, and modpack.
  - Updated [`AGENTS.md`](AGENTS.md), [`CLAUDE.md`](CLAUDE.md), [`README.md`](README.md), and [`docs/GAME_DESIGN.md`](docs/GAME_DESIGN.md) to require headless dedicated server compatibility at all times.
  - Enforced rules against client-class references in common/server logic, singleplayer-only assumptions, and shared player-state leaks.

### [2026-11-06] Minecraft Modding Developer Skill Integration
- **Commit:** `9459ba5`
- **Scope & Details:**
  - Integrated the full-stack `minecraft-modding` agent skill into [`.agents/skills/minecraft-modding/SKILL.md`](.agents/skills/minecraft-modding/SKILL.md).
  - Configured workflows for NeoForge 1.21.1 / Java 21, including Gradle/ModDevGradle commands, `DeferredRegister` patterns, event bus lifecycles, JSON datagen wiring, and standard asset checklists.

### [2026-11-06] Obsidian Interactive Skill Tree Suite Import
- **Commit:** `3df5744`
- **Scope & Details:**
  - Added the complete interactive Obsidian skill tree vault (`Obsidian Skill Tree Nodes/`) authored by Spencer.
  - Includes 4 class build planners (`Warrior`, `Mage`, `Archer`, `Priest`), 4 purchase ledger `.base` tables, 5 visual `.canvas` maps, and 128 individual markdown node notes across all 4 classes and 8 subclasses.

### [2026-11-06] Canonical Skill Tree Design Specification
- **Commit:** `bdc4b51`
- **Scope & Details:**
  - Established canonical [`docs/SKILL_TREES.md`](docs/SKILL_TREES.md) (balancing draft 2.1), settling core character progression and combat rules.
  - Locked 128 total nodes (32/class), 49 SP budget across levels 2–50, 64 SP cost for shared tree + 1 subclass branch (enforcing permanent scarcity), and progression level gates (25, 27, 30, 35, 40, 45 Ascension).
  - Codified combat stacking caps: +25% attack speed, 35% damage resistance, 30% max CDR, single-bucket +50% armor set multiplier, and no recursive proc loops.

### [2026-11-05] Initial Game Design Manuscript & Agent Guidelines
- **Commits:** `c190b55`, `1dc7f57`
- **Scope & Details:**
  - Uploaded author storyline manuscript [`docs/GAME_DESIGN.md`](docs/GAME_DESIGN.md) covering Act I (*The Illusion of Ever Spring*), Act II (*The Waking Nightmare*), world mechanics, bosses (*Frost Yeti*, *Black Dragon*, *Undead Yeti*), Ether Tears, and Pocket Hub.
  - Added [`CLAUDE.md`](CLAUDE.md) agent instructions aligned with binding [`AGENTS.md`](AGENTS.md).
  - Preserved third-party mod research and compatibility matrix ([`docs/MOD_COMPATIBILITY_RESEARCH.md`](docs/MOD_COMPATIBILITY_RESEARCH.md)).

