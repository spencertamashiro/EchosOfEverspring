# Minecraft RPG Mod: Game Design Manuscript

**Source status:** Preserved from **Rough Draft 1.0** (author storyline and core systems manuscript), paired with reconciliation notes against the binding invariants in [`AGENTS.md`](../AGENTS.md). Where Rough Draft 1.0 numbers differ from newer balance invariants in `AGENTS.md` or upcoming `docs/SKILL_TREES.md`, both are explicitly noted so nothing is silently overwritten or promoted without an ADR.

---

## 1. Core Systems & Mechanics

- **Engine & Framework:** Minecraft Java Edition (**NeoForge**, suggested baseline `1.21.1` / `NeoForge 21.1.x` / `Java 21`), blending Souls-like exploration and combat with Hypixel Skyblock progression.
- **Currency & Economy:** **Gold** dropped from enemies is the primary currency.
  - **Chest Shops (Layer 1):** Used for consumables and potion/buff restocking.
  - **Podium Shops (Layer 2):** Skyblock-style podiums used for rare artifacts (locked until late game via the *Dark Egg*).
  - *Implementation invariant (`AGENTS.md` §1):* All Gold transactions and one-time item rewards are server-authoritative, atomic, and idempotent.
- **The Ether Tears:** Fast-travel waypoints placed across the handcrafted world. Unlocking an Ether Tear clears "fog of war" for its region on a custom fullscreen map GUI (**Hotkey: `M`**).
- **The ID Card:** Inventory item acting as a custom GUI. Displays:
  - Player Level
  - Completed Commissions
  - Current Gold
  - Character Stats
- **Damage Types:**
  - `Physical` (`Slashing` / `Blunt`)
  - `Magic`
  - `Holy`
  - `Frost`
  - `Dark`
- **Multiplayer Scaling:** Bosses gain increased HP based on party size and utilize **Aggro/Threat** mechanics, targeting the player dealing the highest DPS.

---

## 2. Classes & Stats

Classes are determined by equipped armor sets. Equipping a **full set** grants:
1. A passive class buff
2. A unique armor set effect
3. A relevant-class-damage multiplier

> **Balance Reconciliation Note (`AGENTS.md` §1 vs. Rough Draft 1.0):**
> - **Rough Draft 1.0** specified a `200%` damage multiplier for wearing a full set.
> - **`AGENTS.md` §1 (Updated Invariant)** locks the full-set bonus to a **`+50%` (`1.5x`) relevant-class-damage multiplier applied once** (from a single authoritative modifier source).
> - **Mixed-Armor & Shared Sets:** Rough Draft 1.0 noted Archer sharing Light Armor with Warrior. Because `AGENTS.md` §1 defines classes strictly by full armor sets, distinct class armor tags/sets and the exact mixed-armor rule require a formal ADR before implementation.
> - **Subclasses & Skill Trees (`AGENTS.md` §1 / `docs/SKILL_TREES.md`):** Each base class branches at Level 25 into two subclasses (8 total), with 32 skill tree nodes per class (12 shared + 10 per branch × 2) and a Level 50 cap (49 total SP).

### The Warrior
- **Gear:** Leather/Heavy Armor, Swords, Blunt Weapons.
- **Draft 1.0 Passive:** `+20%` Movement Speed.
- **Playstyle:** High mobility, physical damage, melee-range parries and dodges.
- **Subclasses (`AGENTS.md` §1):** *Duelist* / *Juggernaut*.

### The Mage
- **Gear:** Mana Robes (boosts Max Mana pool), Magic Staves.
- **Draft 1.0 Passive:** `-10%` Mana Consumption.
- **Playstyle:** Staves intercept mouse clicks:
  - **Left-Click:** Close-range AoE burst.
  - **Right-Click:** Long-range homing/sniper spell.
- **Subclasses (`AGENTS.md` §1):** *Battlemage* / *Arcanist*.

### The Archer
- **Gear:** Light Armor, Bows, Crossbows. *(See note above regarding armor set distinction from Warrior).*
- **Draft 1.0 Passive:** `30%` chance to deal `3x` Critical Damage.
- **Playstyle:** Physical ranged DPS. Requires distance to maximize single-target damage.
- **Subclasses (`AGENTS.md` §1):** *Marksman* / *Skirmisher*.

### The Priest
- **Gear:** Light Robes (Holy tuning), Catalysts/Chimes.
- **Draft 1.0 Passive:** `+50%` Incoming Healing; `2x` Base Damage against Demons and Undead.
- **Playstyle:** Support hybrid. Casts spells that erode enemy HP while siphoning health to self or multiplayer allies.
- **Subclasses (`AGENTS.md` §1):** *Hierophant* / *Inquisitor*.

---

## 3. Act I: The Illusion of Ever Spring (Prologue)

### Zone 1: The Tutorial Village & Forest
- **Narrative:** A bright, peaceful start. NPCs teach the player the basics.
- **Mechanics:**
  - Player is given a rusted weapon.
  - Learns combat on a Training Dummy.
  - Earns first Gold to buy a potion.
  - Activates the first **Ether Tear**.
- **Progression:** Player farms basic mobs (*Slimes*, *Wights*) in the adjacent forest to reach **Level 5**.
- **Progression Gate:** **Level 5** is required to show the **ID Card** to the guards and cross the drawbridge into the First City.

### Zone 2: First City & The Mountain Pass
- **Mechanics:** Complete daily commissions (finding herbs, giving advice) to gain XP and earn the first Class Gear sets.
- **The Inn Event (Missable Quest):** The player is warned of an approaching storm.
  - **Choice A (Rest):** Time advances and a thunderstorm begins. The player receives a quest for a **Bejeweled Handle** (crafting material for an early-game sword).
  - **Choice B (Ignore):** Miss out on the quest entirely.
- **Mountain Mobs:** *Frostborn* enemies that inflict **Frostbite** (slows attack speed and drains stamina).

### Boss 1: The Frost Yeti
- **Phase 1 (100% – 50% HP):** Slow, highly telegraphed AoE slams. Teaches the player to dodge and manage stamina.
- **Phase 2 (<50% HP):** Highly delayed attacks designed to punish panic-dodging, accompanied by constant AoE frost damage.
- **Rewards (Server-Authoritative One-Time Grant):**
  - **10 Levels**
  - **Choice of:** **Frost Heart** (Mage Staff material) **OR** **Yeti Shard** (Warrior Sword material).
  - Unlocks **Archer** and **Priest** gear tiers.

### Zone 3: The World Tree (Central Hub)
- **Location:** Monster-free elven sanctuary governed by **Camilia**.
- **Facilities:**
  - **Storage:** Double chests and universal Ender Chest.
  - **Training Hall:** Indestructible DPS dummy that outputs damage numbers into chat.
  - **Merchant's Guild (Layer 1):** Chest UI for potion/buff restocking.
  - **Secret Basement (Layer 2):** Skyblock-style podiums. Locked until late game (requires *The Dark Egg*).
  - **Workshop:** Custom blueprint UI anvil system for combining materials into weapons.

### Zone 4: Second City & The Capital
- **Progression:** Player must reach **Level 30** via bounties and farm **1,000 Gold** to bribe the Capital Guards.
- **Narrative:** The King is dying. A dragon attack is imminent.
- **The King's Gift:** **Champion's Sword** (scales with Holy/Magic/Physical, deals `2x` damage to Dragons).

### Boss 2: The Black Dragon
- **Mechanics:** Constant aerial dogfight (no perching, no towers). The sky turns black. Requires ranged attacks or timed jump-strikes as the dragon swoops.
- **Rewards:**
  - **Choice 1:** **100,000 Gold** **OR** a Mid/Late-game weapon (Sword / Bow / Staff / Holy Catalyst).
  - **Guaranteed Drop:** **Corrupted Heart** (Mage), **Dragon Claw** (Warrior), **Dragon Horns** (Archer), or **The Dark Egg**.
  - **The Dark Egg:** Serves as the key to unlock the **Layer 2 Basement Shop** in the World Tree.

---

## 4. Act II: The Waking Nightmare (The True World)

### The Reality Shift
- **Mechanics:** The player falls asleep and is teleported to a parallel dimension with **1:1 coordinates** corresponding to the Act I world.
- **World & Travel State:**
  - **All Ether Tears are reset except the World Tree Hub.**
  - The skybox is permanently locked to a dark void.
  - Biomes and structures are replaced by their hand-authored corrupted counterparts.
  - *Implementation invariant (`AGENTS.md` §6):* Scripted story flags persist across the dimension transition, and the Act II transition is one-time and non-farmable.

### Consequences & Re-Traversal
- **Tutorial Village:** Destroyed. Formerly friendly NPCs are now high-level zombies that the player must kill.
- **Innkeeper Check (Consequence Branch):**
  - **If the player killed her earlier:** She drops a unique sorrowful lore artifact / upgrade material.
  - **If spared:** She spawns as a terrifying **Demon Mini-Boss** outside the ruins of the Second City.

### Boss 3 (Rematch): Undead Yeti
- **Mechanics:** 5 Dark Mages spawn around the arena perimeter casting homing dark fire. The Undead Yeti uses its **Phase 2** moveset immediately from 100% HP.
- **Rewards:** None. Pure survival check.

### The Fall of the Hub
- **Narrative:** The World Tree is burning. **Camilia** is dying at the roots.
- **The Pendant:** Camilia gives the player the **Pocket Hub Pendant**. Right-clicking this item opens a spectral UI to access the Ender Chest, class swapping, and basic forging—permanently replacing the physical World Tree hub.

### The Capital Climax
- **Environment:** A massive rift leaking white Ether splits the earth. 3 Corrupted Dragons circle the ruins of the Capital.
- **The Final Entity:** A gargantuan, skeletal 3-headed beast suspended in the dead sky, feeding on the remnants of the kingdom. *(Detailed boss phase mechanics TBD).*

