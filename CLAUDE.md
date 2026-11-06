# CLAUDE.md — Claude Code & AI Agent Guide for `tentativeRPG`

Welcome! This file configures Claude (Claude Code, IDE integrations, and conversational sessions) for working on **`tentativeRPG`** alongside co-contributors **Dylan** and **Spencer**.

> **Binding Precedence:** [`AGENTS.md`](AGENTS.md) is the binding contract for all AI agents in this repository. This `CLAUDE.md` distills and structures those rules specifically for Claude workflows while linking directly to the canonical design and research documents. Never contradict [`AGENTS.md`](AGENTS.md).

---

## 1. Project Identity & Baseline Stack

- **What we are building:**
  1. **One single custom NeoForge RPG mod JAR** containing all original RPG gameplay (classes, subclasses, 128-node skill trees, combat/damage math, Gold economy, story/quest state machine, Ether Tears & Fog-of-War map, ID Card GUI, Pocket Hub Pendant, and boss mechanics).
  2. **An independently authored, handcrafted Minecraft campaign world** across two states: **Act I: The Illusion of Ever Spring** and **Act II: The Waking Nightmare** (1:1 coordinate parallel dimension).
  3. **An optional launcher-managed modpack** (`.mrpack` / CurseForge manifest) and staging installer helpers.
  4. **Dedicated Server Implementation (Eventually Planned):** A dedicated server deployment is planned for multiplayer hosting. Keep this in mind across all architectural decisions: enforce strict client/server separation, guarantee headless dedicated server compatibility, prevent client-only classloading in common/server paths, and design for robust multiplayer synchronization and isolated player state.
- **Target Platform Baseline:** Minecraft **1.21.1** / **NeoForge 21.1.x** / **Java 21** (verify `gradle.properties` once the NeoForge MDK is initialized; do not invent dynamic/unverified dependency versions).
- **Never split gameplay into multiple custom mods.** External third-party mods are libraries or external content wrapped behind `compat/<modname>/` adapters.

---

## 2. Essential Reading Before Making Changes

Before writing or modifying code, always inspect:
1. `pwd` and `git status --short` — verify what actually exists on disk (do not assume planned directories or Gradle files exist until committed).
2. [`AGENTS.md`](AGENTS.md) — non-negotiable product invariants, third-party mod gates, map safety, and required task output format.
3. [`docs/GAME_DESIGN.md`](docs/GAME_DESIGN.md) — full Act I & Act II storyline, zones, bosses, shops, Ether Tears, Pocket Hub, and Draft 1.0 vs. `AGENTS.md` reconciliation notes.
4. [`docs/SKILL_TREES.md`](docs/SKILL_TREES.md) — canonical 128-node skill trees, 8 subclasses, level gates, and combat formulas.
5. [`docs/MOD_COMPATIBILITY_RESEARCH.md`](docs/MOD_COMPATIBILITY_RESEARCH.md) — third-party mod eligibility, licensing, and integration POC rules.
6. [`UPDATES.md`](UPDATES.md) — project activity, recent changes, and detailed evolution logs.
7. `docs/decisions/` *(when present)* — Architecture Decision Records (ADRs) take top design precedence.

---

## 3. Core Gameplay & Architecture Invariants

### Classes, Armor & Subclasses
- **4 Armor-Defined Base Classes:** `Warrior`, `Mage`, `Archer`, `Priest`.
- **Full Armor Set Requirement:** Wearing a complete matching class set grants:
  - The class passive
  - The unique set effect
  - **`+50%` (`1.5x`) relevant-class-damage multiplier applied once** (single authoritative bucket; note that `docs/GAME_DESIGN.md` Rough Draft 1.0 mentioned `200%`, which was superseded by `+50%` in `AGENTS.md` §1).
  - *Blocker note:* Mixed-armor behavior and Warrior/Archer light-armor differentiation require an ADR before coding mixed-set edge cases.
- **8 Subclasses (2 per class, unlocked at Level 25):**
  - **Warrior:** *Duelist* / *Juggernaut*
  - **Mage:** *Battlemage* / *Arcanist*
  - **Archer:** *Marksman* / *Skirmisher*
  - **Priest:** *Hierophant* / *Inquisitor*
  - Only **one** subclass can be active at a time. Weapons gate subclass effects, but equipping a weapon never auto-selects an unpurchased subclass.

### Skill Trees & Leveling
- **Level Cap:** `50` (`49 SP` total earned from levels 2–50).
- **128 Total Nodes (32 per class):** 12 shared nodes (`24 SP`) + 2 subclass branches of 10 nodes each (`40 SP` per branch). Completing the shared tree + one branch costs `64 SP`, meaning **players cannot max everything**—build scarcity is mandatory. Never add farmable or exploitable bonus SP.
- **Level Gates:**
  - Level `25`: Subclass unlock
  - Level `27`: Subclass Passive slot (choose 1 of 4 per subclass, `0 SP` selection cost)
  - Level `30`: Subclass Active slot (choose 1 of 4 per subclass, `0 SP` selection cost)
  - Level `35`: Advanced nodes
  - Level `40`: Endgame nodes
  - Level `45`: Ascension nodes (**mutually exclusive**)

### Damage Types & Combat Authority
- **Damage Types:** `Physical` (`Slashing` / `Blunt`), `Magic`, `Holy`, `Frost`, `Dark`.
- **Immutable Stat Snapshots:** Compute stats and damage deterministically on the **logical server**.
- **Exploit Prevention:** Armor swapping, death, relogging, dual-wielding, weapon tag switching, or status proc recursion must never bypass cooldowns, mana/stamina costs, or unlock gates.
- **Server Authority & Dedicated Server Target:** The server owns all damage calculation, skill unlocks, Gold balances, shop purchases, quest flags, boss choice rewards (*Frost Heart* vs. *Yeti Shard*, *100,000 Gold* vs. weapon, *Dark Egg*), and Ether Tear teleport permissions. The client only renders UI and sends intent packets. Because a dedicated server deployment is eventually planned, never couple game state to singleplayer integrated server assumptions.

### Story & World State (`Act I` → `Act II`)
- **No Procedural Campaign Generation:** Both Act I (*The Illusion of Ever Spring*) and Act II (*The Waking Nightmare*) are hand-authored maps with 1:1 coordinate mapping. Never generate terrain, structures, or guessed coordinates in code.
- **Act II Reality Shift:** Teleporting to the Act II dimension preserves scripted story flags (e.g., whether the Innkeeper was killed or spared, whether the missable Storm/Inn *Bejeweled Handle* quest was completed), **resets all Ether Tears except the World Tree Hub**, and cannot be repeated/farmed.
- **The Fall of the Hub:** In Act II, when the World Tree burns, Camilia grants the **Pocket Hub Pendant** (right-click spectral UI for Ender Chest, class swapping, and basic forging).

---

## 4. Repository Package & Directory Conventions

When Java source is initialized, keep everything inside **one mod** under `src/main/java/<base_package>/` using focused subpackages:
- `registry` — DeferredRegisters for items, blocks, entities, attachments, menus, payloads
- `player` — Player state attachments, ID Card data, persistent flags
- `progression` — XP, levels (1–50), SP ledger (`49 SP` cap enforcement)
- `classsystem` — Armor-set detection, class passives, active subclass state
- `skills` — Skill tree graph loader, validation (cycles, reachability, gates, mutual exclusion), unlock logic
- `abilities` — Active/passive ability execution, cooldowns, mana/stamina costs
- `stats` — Immutable stat snapshots, single-bucket modifier math
- `combat` — Server hit resolution, damage types (`Slashing`, `Blunt`, `Magic`, `Holy`, `Frost`, `Dark`), parry/dodge, boss threat/aggro
- `magic` — Staff left/right click interceptors, priest catalysts/siphoning
- `equipment` / `items` — Weapons, armor sets, *ID Card*, *Pocket Hub Pendant*, *Dark Egg*, crafting materials
- `economy` — Authoritative Gold ledger, Layer 1 Chest Shops, Layer 2 Podium Shops, Blueprint Anvil Workshop
- `story` — Act I/II state machine, commissions, missable Inn Event, Innkeeper branch, one-time boss reward transactions
- `travel` / `map` — Ether Tears, fullscreen map fog-of-war sync (`M` key), Act II dimension transition
- `encounters` / `bosses` — *Frost Yeti*, *Black Dragon*, *Undead Yeti* + 5 Dark Mages, *Demon Mini-Boss*, *Final 3-Headed Entity*
- `world` — Region/POI ID registry (no hardcoded/guessed coordinates)
- `networking` — Versioned NeoForge custom payloads (client request -> server validation -> state sync)
- `commands` — Admin/debug/testing commands
- `client` — Screens, HUD, keybindings, GeckoLib renderers (strictly isolated from dedicated server classloading!)
- `compat` — Isolated adapters (`compat/<modname>/`) with safe classloading boundaries

**Do not scatter empty placeholder classes across all packages prematurely.** Build vertical slices with real logic and unit tests.

---

## 5. Third-Party Mod Integration Rules

1. **Isolated Adapters (`compat/<modname>/`):** Core RPG classes (`stats`, `combat`, `story`, `skills`) must never directly import third-party mod internals. Guard optional integrations against eager JVM classloading when a mod is absent.
2. **Mutually Exclusive Choices:**
   - **Combat:** Custom vanilla-event combat **OR** Better Combat **OR** Epic Fight (never combine Better Combat + Epic Fight).
   - **Recipe Viewer:** JEI **OR** EMI (not both).
   - **Renderer:** Sodium **OR** Embeddium (not both).
   - **Fast Travel:** Exclude Waystones (our Ether Tears & Pocket Hub own travel).
   - **Map:** Exclude FTB Chunks / minimaps that bypass our Fog-of-War (`M`) system.
3. **Quest & NPC Authority:** If FTB Quests or Easy NPC is used, it is strictly a **presentation layer**. All Gold, XP, unlocks, and irreversible quest flags must be validated and granted idempotently by our mod's server service.
4. **No Unapproved Redistribution:** Never commit third-party mod JARs to Git or scrape/copy third-party assets/models/code.

---

## 6. Verification & Output Protocol for Every Task

1. **Test Pure Logic First:** Whenever implementing stat math, armor set detection, skill point scarcity (`49 SP` max), node prerequisites, level gates (`25/27/30/35/40/45`), mutual exclusions, or idempotent reward transactions, write fast unit tests (`./gradlew test` once Gradle is initialized).
2. **Client/Server Separation:** Never reference `net.minecraft.client.*` or client-only renderer classes in common/server code paths.
3. **Honest Status Reporting:** Never claim a build, client run, dedicated server boot, or mod integration passed unless you actually executed the command and observed the log output.
4. **Mandatory Task Summary Checklist (`AGENTS.md` §8):** At the end of every substantive task, explicitly report:
   - **(a) Files changed**
   - **(b) Important assumptions vs. confirmed sources**
   - **(c) Dependencies added** (with pinned IDs, licenses, and official URLs, if any)
   - **(d) Tests/commands run** plus actual pass/fail output
   - **(e) Multiplayer & world impact**
   - **(f) Unresolved issues & recommended next vertical slice**
5. **Elaborate Updates in `UPDATES.md` (Preserve Brief `README.md`):**
   - Keep [`README.md`](README.md) strictly concise as a high-level project summary and directory index. Do not expand `README.md` with detailed progress logs or changelogs.
   - When completing substantive milestones, design decisions, or vertical slices, record elaborate entries in [`UPDATES.md`](UPDATES.md) detailing date, commit/scope, affected systems, and technical rationale.

