# Combined Echoes of Everspring playtest

## Try it

With Java 21, run **gradlew.bat runClient "-PplaytestWorld=Echoes of Everspring Playtest"** in the parent project. The prepared save is local. For a new checkout, run **gradlew.bat runClient** and create an isolated Creative test world. Press **K** for the skill menu. Use **/rpgtestingnpc demo** on open ground with operator permission for the sample residents. Follow the [tooltip example](TOOLTIP_ATTRIBUTES.md) for a tagged gear item.

## Implemented project features

- **Residents and dialogue.** Twelve bundled Easy NPC residents support right-click conversations, typewriter text, click-to-reveal, branching greeting/lore/quest responses, voice clips and configurable frames. The five frame themes are Astral, Tide, Cinder, Aurora and Clockwork. Operator commands place the demo cast, spawn one resident, or bind dialogue to an existing Easy NPC. Authors can define custom conversation-only or quest NPCs with JSON, custom colors and optional resource-pack audio.
- **NPC-driven FTB quests.** Bracken Reach contains eleven sample assignments and Fog Beacon contains one. The server validates accept and turn-in interactions, checks and consumes supplies, and records team quest progress. Eligible rewards are claimed in the FTB quest book. The build generates the bundled quest chapters, and custom quest NPC definitions generate a Custom NPC Assignments chapter.
- **Bundled assignments.** Tamsin Reed — A Line Across; Puck Thistledown — The Soft Trail; Orin Flint — Lights on the Palisade; Maela Quill — The Waterlogged Register; Brann Coppervein — Pressure Test; Mire-of-Stars — A Vessel for Starlight; Elowen Vale — Lunch on Credit; Nia Stormglass — Relay Five; Vesper Ink — Counterseal; Sol Emberwake — Ember Oath; Rowan Bell — The Morning Bell; Sable Fen — A Light in the Fog.
- **Gear cards and inventory decor.** Tagged items show painted, animated tooltip cards with item art, rarity, class and element labels, damage, crit, stamina, mana, armor, utility descriptions and lore as applicable. The metadata supports twelve gear kinds, six rarities, four class labels and twenty-one visual styles. It provides readable narration text and a validated Java metadata API. Rose and thorn ornaments decorate inventory/container corners.
- **Everspring skill trees.** Warrior (Duelist/Juggernaut), Mage (Battlemage/Arcanist), Archer (Marksman/Skirmisher) and Priest (Hierophant/Inquisitor). There are 128 paid nodes and 12 free class/subclass crests. The playtest starts with 49 SP per class. Purchases enforce tier requirements, subclass exclusivity and one Ascension per specialization. Tempered Steel applies its attack-damage modifier. The game preserves native skill state via Pufferfish.
- **Skill-tree presentation.** Four distinct landscape tabs, a weapon-themed landscape for the RPG Series Weapon Skills tab, spaced-out node layouts, filtered decorative geometry, and thin, single-color connections. The stock weapon graph keeps its progression and shared budget.

## Loaded integrations

Easy NPC Core supplies NPC entities. FTB Quests, Library, and Teams provide quest UI and team progress. Tooltip Overhaul provides client tooltip rendering support. Pufferfish's Skills and Skill Tree (RPG Series) provide the skill menu and stock weapon tree. Spell Engine, Spell Power and Ranged Weapon API provide the upstream spell and weapon framework. Accessories, Curios, owo-lib, Forgified Fabric API, Player Animator, Cloth Config and Architectury provide runtime support.

The [original dependency guide](PROTOTYPES.md) covers the NPC, quests and tooltip stack. The [skill dependency lock](skill-dependencies.lock.json) records version IDs, licenses, official download URLs and checksums for the skill stack. Local staging under run is ignored by Git. Use the parent JAR in the combined instance.

## Prototype boundaries

The bundled cast and flat playtest are development content. Gear-card stats describe items. The other Everspring combat effects and 64 ability options are design previews. Campaign level and quest gates, armor-set class assignment, weapon conditions, ability equip/casting, broader combat and economy, the authored campaign world, Ether Tears, Pocket Hub, fog-of-war, and campaign bosses remain design work. Dedicated GameTests use simulated players. Live remote multiplayer remains unverified.
