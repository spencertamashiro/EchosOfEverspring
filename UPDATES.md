# UPDATES.md — Project Activity & Changelog

This document tracks detailed progress logs, milestone summaries, and architectural evolution for `tentativeRPG`. High-level repository navigation and identity remain in [`README.md`](README.md) to keep its word count concise.

---

## Update Log

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

