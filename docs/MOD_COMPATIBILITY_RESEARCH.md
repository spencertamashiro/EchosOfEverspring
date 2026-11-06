# Third-Party Mod Audit — NeoForge 1.21.1 / Handcrafted RPG

**Research snapshot:** November 5, 2026. **Status:** public listings and publisher docs inspected; **none of these combinations has been installed, compiled, or gameplay-tested in this repository**. The presence of a 1.21.1 NeoForge release is *file-level eligibility*, not evidence of compatibility with our mod or with the full pack. Each citation below is a canonical project, published version, original-license page, or first-party platform policy—not a JAR mirror.

## Executive selection

1. **Start with:** NeoForge 1.21.1 + Java 21, our one RPG mod, GeckoLib, and a recipe viewer (JEI *or* EMI). Run custom combat first as a baseline.
2. **Prototype before deciding:** Curios, Easy NPC Core/Config UI, FTB Quests + prerequisite libraries, Lootr, and **one** combat engine alternative. Keep each behind a `compat/` adapter.
3. **For hand building:** vanilla structure blocks; WorldEdit in a *builder-only profile*. If the map uses FramedBlocks, Macaw's Bridges or Supplementaries blocks, those exact mods become **mandatory runtime dependencies for that world**.
4. **Optimize late:** Lithium, ModernFix, FerriteCore, Sodium; enable independently and test custom UI, world blocks and combat animations. Keep spark in developer/server diagnosis tools.
5. **Normally exclude:** Waystones (duplicates Ether Tears), FTB Chunks/map revealing mods (story map protection and spoilers), procedurally generated dungeon/biome mods, overlapping RPG-level systems, multiple combat engines, competing spell-economy mods.
6. **Distribution:** use platform-approved manifest references and original unmodified files, with attribution/permission checks on the *specific* pack platform. Never equate open downloads with permission to republish a JAR. Never repackage the Minecraft game.

### Classification legend

- **V** — confirmed *published* Minecraft 1.21.1 + NeoForge file or a publisher's version page. Does **not** mean verified to co-run.
- **C** — candidate; requires a separate integration proof of concept (POC), dependencies and licensing review.
- **D** — deliberately deferred/excluded (may have available version; rejection is a **design decision**, not necessarily technical incompatibility).
- **B** — builder-only tooling, excluded from player runtime.
- **Distribution column:** `manifest/permissions` = retrieve through authorized platform manifest only after checking its approval rules; `notice` = include upstream copyright/license/attribution as appropriate; `separate assets` = code license not an asset license. No row means unconditional self-hosting rights.

## Research matrix

| Project | Published NeoForge 1.21.1? / evidence | Rights / pack status | Use in **our** game | Risk / decision |
|---|---|---|---|---|
| **GeckoLib** | **V**, [Modrinth project](https://modrinth.com/mod/geckolib) | MIT listing; include notices if copying MIT-covered code | Core animated boss, weapon and ability models | **Adopt after clean POC**; API version pin; asset designs must be ours |
| **Curios API** | **V**, [Curios versions](https://modrinth.com/mod/curios/versions) | LGPL-3.0-or-later [listing](https://modrinth.com/mod/curios/versions) | Optional relics, rings, artifacts | Integrate slots only; no armor/class duplicate effects |
| **Easy NPC Bundle / Core / Config UI** | **V**; [bundle](https://modrinth.com/plugin/easy-npc), [Core](https://modrinth.com/mod/easy-npc-core), [Config UI version](https://modrinth.com/mod/easy-npc-config-ui/version/NDcoVqYE) | **MIT applies to code only**; [asset terms allow unmodified original JAR in public packs](https://www.curseforge.com/minecraft/mc-mods/easy-npc-core/license). Never extract/copy art | Camilia, city merchants, class trainers, dialogue | **POC**. Bundle is a dependency convenience, not a monolithic redistributable; avoid installing conflicting obsolete jar-in-jar and modules together |
| **FTB Quests (NeoForge)** | **V**, [FTB Quests releases](https://www.curseforge.com/minecraft/mc-mods/ftb-quests-forge) | ARR on listing; pack via CurseForge manifest and terms; no code/art copying | Player-facing quest journal and repeatable commissions | **POC**. Our mod alone owns irreversible story flags and unique rewards |
| **FTB Teams + FTB Library + Architectury** | **V** as declared [FTB Quests file dependencies](https://modpacks.ch/mods/minecraft/289412/ftb-quests-neoforge/versions/8842729); [FTB Library publisher](https://www.curseforge.com/minecraft/mc-mods/ftb-library-forge) | FTB Library **ARR**; use platform manifest | Required components if FTB Quests selected | **Must choose compatible complete version set**; don't assume arbitrary newest are compatible |
| **FTB XMod Compat** | **V**, [NeoForge 21.1.14 file](https://www.curseforge.com/minecraft/mc-mods/ftb-xmod-compat/files/9344504) | ARR; CurseForge manifest | Only if needing FTB Quests/JEI/KubeJS bridge | Version **21.1.14 requires FTB Library 2101.1.38+**. Do not install if that matching Library release is unavailable or unverified |
| **KubeJS** | **V**, [1.21.1 NeoForge build / LGPLv3](https://www.curseforge.com/minecraft/mc-mods/kubejs/license) | LGPLv3; notices / platform handling | Pack recipes, tags, basic loot tweaks | Optional; **not** owner of core class/XP/story, version may need Rhino/other dependency inspection |
| **JEI** | **V**, [1.21.1 NeoForge file](https://www.curseforge.com/minecraft/mc-mods/jei/files/8946440) | MIT; [publisher explicitly permits modpacks](https://www.curseforge.com/minecraft/mc-mods/jei) | Recipe/ingredient browser | **Preferred** if integrating FTB; a 1.21.1 file inspected is beta, select a supported exact build |
| **EMI** | **V**, [1.21.1 NeoForge release](https://modrinth.com/mod/emi/version/1.1.24%2B1.21.1%2Bneoforge) | MIT | Alternative recipe browser | **Choose one UI**; EMI supports JEI runtime recipe compatibility but avoid installing both without need |
| **WorldEdit** | **V**, [publisher's 1.21.1 page](https://modrinth.com/plugin/worldedit?version=1.21.1) | GPLv3 and **explicit modpack permission** on project page | Manual sculpting, schematics, batch editing | **Builder-only**; not required at runtime unless an authored feature truly depends on it |
| **FramedBlocks** | **V**, [1.21.1 NeoForge / LGPLv3](https://www.curseforge.com/minecraft/mc-mods/framedblocks) | LGPLv3 | Custom-shaped blocks for capitals, dungeons | **Strong map-art candidate**; becomes hard runtime dependency once blocks used |
| **Macaw's Bridges** | **V** (publisher supports 1.21.1; [project](https://www.curseforge.com/minecraft/mc-mods/macaws-bridges)) | ARR, but publisher says modpack use **permitted with credit and source link** | Bridges in authored areas | Optional; exact NeoForge file and bridge-block migrations must be checked before using in final world |
| **Supplementaries + Moonlight Library** | **V**, [Supplementaries](https://modrinth.com/mod/supplementaries) ([1.21.1 releases indexed](https://mcreference.com/mods/supplementaries)) | Custom Supplementaries Team license; author **explicitly permits packs**; Moonlight is required; respect contributed asset licenses | Signposts, lanterns, decor, puzzles | **Good map-art candidate**, but sizeable content and runtime dependency; disable unintended mechanics in pack configs |
| **Chipped** | **Candidate**, [1.21.1 support index](https://mcreference.com/mods/chipped) | ARR per index; verify primary release/permissions before pack | Extensive building block variants | **Defer until publisher exact-file and rights review** |
| **Patchouli** | **V**, [1.21.1 NeoForge release](https://www.curseforge.com/minecraft/mc-mods/patchouli/license) | **CC-BY-NC-SA-3.0**, project text explicitly allows modpack inclusion with credit | Optional in-game lore manual | **Defer**; overlaps quest/book UI, and noncommercial clause deserves review for commercial plans |
| **Lootr** | **V**, [1.21.1 NeoForge file](https://www.curseforge.com/minecraft/mc-mods/lootr/files/8695784) | MIT listed for inspected release; note another aggregated overview reported “Custom License”: reconfirm the **selected version's** legal terms | Per-player repeatable exploration loot | **POC**; never hand one-time quest relics / Gold to Lootr alone |
| **Better Combat** | **V**, [1.21.1 NeoForge published file](https://modrinth.com/mod/better-combat/version/2.3.1%2B1.21.1-neoforge) | ARR; manifest/permissions; required **Player Animator** and **Cloth Config** | Weapon swings, combo attack controller | **High-risk POC** competing with custom damage, parries, stamina, heavy attacks |
| **Epic Fight** | **V**, [1.21.1 NeoForge publisher file](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod) | GPLv3 on publisher | Full Souls-like attack engine alternative | **High-risk POC**, choose **instead of**, not alongside Better Combat |
| **Combat Roll** | **V**, [NeoForge + 1.21.1 project](https://modrinth.com/mod/combat-roll) | GPL-3.0-only | Dodge/roll movement if delegated | **Defer** until combat owner selected; dodge cost, i-frames, class skills will otherwise conflict |
| **Mowzie's Mobs** | **V**, [1.21.1 NeoForge publisher listing](https://modrinth.com/mod/mowzies-mobs) | Custom **Mowzie's Mobs License**; check original terms; no assumed permission to rip sounds/models | Optional encounters manually spawned in predefined arenas | **POC** only; disable undesired natural spawns/worldgen, external bosses remain external |
| **L_Ender's Cataclysm** | **V**, [NeoForge 1.21.1 release and license](https://www.curseforge.com/minecraft/mc-mods/lendercataclysm/license) | **Assets reserved; code CC BY-NC-ND 4.0** according to author | Optional imported bosses/creatures intact | **High content/balance risk**, no reskins/extracted animations; disable structural generation on handmade map |
| **Iron's Spells 'n Spellbooks** | **V**, [publisher release/license](https://www.curseforge.com/minecraft/mc-mods/irons-spells-n-spellbooks/license) | ARR on CurseForge | Optional magic content | **Defer**: independent mana, >100 spells, combat items/bosses and progression clash with Mage/Priest |
| **Ars Nouveau** | **V**, [project](https://modrinth.com/mod/ars-nouveau) | **Licensing metadata inconsistent** between Modrinth views (ARR vs GPL); resolve exact release license | Complex crafted spell/automation system | **Defer**: duplicates core magic and is unnecessary to campaign |
| **Waystones + Balm** | **Candidate**, [Waystones publisher](https://www.curseforge.com/minecraft/mc-mods/waystones) | ARR; published pack files / dependency Balm | Out-of-box teleport | **Exclude**: bypasses our Ether Tears / map discovery; not needed |
| **Paxi** | **V**, [1.21.1 NeoForge / LGPL listing](https://modrinth.com/mod/paxi) | LGPL-3.0-only | Global datapack/resource-pack injection | Optional; worlds can include their own datapacks—don't add without benefit |
| **Jade** | **V**, [1.21.1 NeoForge release](https://modrinth.com/mod/jade/version/eYz2YBGT) | **CC-BY-NC-SA-4.0** on Modrinth; review noncommercial conditions | Tooltips & item info | Optional; must not reveal hidden lore/weaknesses, inspect license if monetizing |
| **Jade Addons** | **V**, [1.21.1 NeoForge listing](https://modrinth.com/mod/jade-addons-forge) | ARR; author permits packs **only on CurseForge/Modrinth** | Optional Jade integrations | Only with Jade and compatible dependencies; no separate redistribution |
| **Sophisticated Backpacks** | **V**, [1.21.1 NeoForge publisher listing](https://www.curseforge.com/minecraft/mc-mods/sophisticated-backpacks) | ARR | Optional inventory convenience | **Defer**: extra portable storage and upgrades impact combat/economy balance |
| **Simple Voice Chat** | **V**, [1.21.1 NeoForge release](https://modrinth.com/mod/simple-voice-chat/version/neoforge-1.21.1-2.6.21) | ARR on Modrinth | Optional coop proximity voice | Only if co-op wants it; needs server UDP/port and distinct player privacy policy |
| **ModernFix** | **V**, [1.21.1 NeoForge](https://modrinth.com/mod/modernfix/version/lVkfC362) | LGPL-3.0-only | Memory/performance/bug fixes | Pilot after core; regression-test UI, registries, resource reload and world serialization |
| **Lithium** | **V**, [1.21.1 NeoForge](https://modrinth.com/mod/lithium/version/mc1.21.1-0.15.2-neoforge) | LGPL-3.0-only | Game tick optimization | Pilot independently, especially for custom AI and entity update sequences |
| **FerriteCore** | **V**, [1.21.1 release family](https://modrinth.com/mod/ferrite-core/versions?l=neoforge) | MIT | RAM reduction | Pilot, verify exact file on installer target and combined footprint |
| **Sodium** | **V**, [1.21.1 NeoForge file](https://modrinth.com/mod/sodium/version/mc1.21.1-0.6.2-neoforge) | **Polyform Shield 1.0.0** (not MIT); source availability ≠ general reuse | Optional client renderer | Test with GeckoLib, HUD, shaders. **Do not install together with Embeddium** without deliberate review |
| **Embeddium** | **Candidate** [project](https://modrinth.com/mod/embeddium) | Exact 1.21.1 NeoForge artifact/license needs recheck | Alternative client renderer | Choose one renderer only; Sodium above has direct 1.21.1 evidence |
| **spark** | **V**, [1.21.1 NeoForge build](https://modrinth.com/mod/spark/version/1.10.124-neoforge-1.21.1) | GPL-3.0-only | Tick, CPU, memory profiling | **Developer/server profile**; analyze combat/AI/teleport stalls, not required gameplay |
| **Axiom** | **Builder-only Fabric**, [Axiom published conditions](https://modrinth.com/mod/axiom) | **Commercial license required for professional commercial building**, other rules on author site | Optional offline map editing on a copy | **Not for NeoForge runtime pack**; confirm world compatibility when transferring a vanilla block-only save |

### Verifiable availability != legal use != integration success

- **Public availability:** an exact file or official listing can be reached. Download from the original publisher/authorized launcher only.
- **Potentially usable:** the file targets the same Minecraft/loader version, dependencies can be resolved, and its *mechanics* fit the RPG.
- **Distribution permissions:** require project/license and platform policy. **Modrinth requires rights checks for each included project**, even when it is listed; authors' conditions take precedence ([policy](https://support.modrinth.com/en/articles/8797527-obtaining-modpack-permissions)). A [CurseForge manifest](https://support.curseforge.com/support/solutions/articles/9000198500-exporting-a-modpack-for-curseforge-project-submission) references hosted project/file IDs; arbitrary override JARs follow different restrictions.
- **Implementation success:** client and **dedicated-server** startup, multiplayer, cross-mod events, gameplay, world preservation, and cold-install replay must all pass. None has been run yet.

## Detailed integration POCs, in priority order

### A. GeckoLib and custom combat animation

Create a custom boss test entity and one animation controller. Confirm renderer is client-only; server determines hit windows independently from animations. Validate dedicated server startup and headless mode, multiplayer observation, and particle budgets. GeckoLib's MIT-listed code permits compatible use, but build **our own original** models, sounds, animation files and credits. Animation timing cannot be the sole basis for granting damage because network lag and animation interruptions exist.

### B. Curios accessories

Implement `AccessoryProvider` adapter with optional-mod classloading boundary, typed item tags, removal/addition event handling and server stat recalculation. Only original chest/helmet/leggings/boots define a base class. A Curios necklace/ring can modify stats but must never create a second +50% armor-set buff. Verify no Curios dependency means the base RPG still starts, if declared optional.

### C. NPC vs story engine

Compare Easy NPC dialogues, trades and actor persistence to our authored story requirements. NPCs have immutable IDs, stable coordinates, per-player story gate presentation and all irreversible server-side transactions must be handled by our mod. Test world migration, runtime script integration, reload, dialogue callback failures, Act II entity replacement, and NPC placement on builders' **copied** save. Art assets in original Easy NPC JAR may be used intact as part of that mod; **not extracted** for our mod.

### D. Quests and commissions

FTB Quests must remain a presentation layer unless a narrow explicit integration proves safe. Your RPG's canonical quest flags, unlocks, Gold, boss milestones and missable one-off quests are stored by the RPG. Define a one-way event bridge and idempotency key so repeated FTB reward commands cannot regrant a Frost Heart or class unlock. Include **Architectury API, FTB Library, FTB Teams** with exact matching FTB Quests build. Skip FTB XMod Compat unless needed; [21.1.14 release notes](https://www.curseforge.com/minecraft/mc-mods/ftb-xmod-compat/files/9344504) require FTB Library `2101.1.38+`—do not assume `2101.1.37` is adequate.

### E. Combat engine decision: own, Better Combat, or Epic Fight

**POC 1** vanilla events + our own minimal hit/damage calculation, stamina, dodge and parry timings (maximum control, largest implementation burden). **POC 2** Better Combat with required Player Animator + Cloth Config (easier animations but cross-mod collision, attack timing and dual wield concerns). **POC 3** Epic Fight (strong Souls-like feel but substantial control of animations and combat rules). Test identical duel/parry/heavy-attack/1v1 and 4-player boss scenarios. Do not combine Better Combat and Epic Fight, and do not add Combat Roll until dodge ownership is explicit. **Do not choose on download count alone.**

### F. Loot and boss content

Lootr only for ordinary refillable/exploration chests, per-player access. Gold and rare story rewards need an immutable, authoritative transaction. External mobs from Mowzie's or Cataclysm can appear as **unmodified installed entities** in a manually scripted arena after disabling natural structure generation and loot/XP exploits; external code/assets may not be copied or modified without license. Native scripted Frost Yeti and Black Dragon mechanics should remain ours if they are signature bosses.

### G. Building blocks and permanent dependencies

WorldEdit is authoring tool. FramedBlocks, Supplementaries/Moonlight or Macaw's Bridges are potential **player/runtime dependencies** the moment their blocks are written into either world. Maintain the exact required mods and stable block IDs in `world/manifests/blocks-used.json`. Test a copied world load with the full installed modpack, then compare counts of unknown/missing blocks and ensure no map artifacts vanish after upgrades. Set max dependency count and budget: prefer **one or two** decoration families at initial map construction, not a dozen.

### H. Performance, UI and accessibility

Profile before optimizing. Try ModernFix, Lithium, FerriteCore with individual smoke tests. Add **one** renderer (Sodium *or* Embeddium) only after animated models/map overlays work. Jade can reveal technical information prematurely; configure spoilers. JEI and EMI are alternatives; do not clutter the player's screen with duplicates.

## POC test template

For each adopted third-party mod, create `docs/compat-tests/<modid>-<version>.md` with:

```yaml
mod: <actual registry ID>
version: <exact release>
minecraft: '1.21.1'
loader: 'NeoForge <pinned patch>'
source_url: <official release page>
sha256: <hash of downloaded approved artifact>
license_url: <exact source>
license_notes: <code, assets, modpack permission and attribution>
dependencies: [<names and exact versions>]
installed_in: [client, server, builder]
client_boot: untested
server_boot: untested
multiplayer: untested
world_roundtrip: untested
cross_mod_events: untested
pack_cold_install: untested
known_conflicts: []
acceptance_owner: <person>
```

Never change `untested` to `pass` without a dated command/log or save-based test. Re-test any affected row when Minecraft, NeoForge, a mod or the world format changes.

## Publisher & platform legal references

- [Modrinth pack permissions](https://support.modrinth.com/en/articles/8797527-obtaining-modpack-permissions)
- [`.mrpack` JSON format, hashes, `downloads`, `overrides` and dependency IDs](https://support.modrinth.com/en/articles/8802351-modrinth-modpack-format-mrpack)
- [CurseForge exporting a pack / third-party override policy](https://support.curseforge.com/support/solutions/articles/9000198500-exporting-a-modpack-for-curseforge-project-submission)
- [Minecraft EULA and modded-client redistribution prohibition](https://www.minecraft.net/en-us/eula)
- [Minecraft Usage Guidelines / commercial-use restrictions](https://www.minecraft.net/en-us/usage-guidelines)
- [NeoForge 1.21.1 MDK and Java 21](https://docs.neoforged.net/docs/1.21.1/gettingstarted/)

**This is a technical/legal triage document, not legal advice or a distribution license. Confirm selected **file-specific** terms again at release.**
