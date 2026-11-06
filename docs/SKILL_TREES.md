# Minecraft RPG Mod — Complete Skill Tree Design

**Design status:** Proposed / balancing draft 2.1  
**Platform:** Minecraft Java Edition (NeoForge)  
**Classes:** Warrior, Mage, Archer, Priest  
**Subclasses:** Duelist, Juggernaut, Battlemage, Arcanist, Marksman, Skirmisher, Hierophant, Inquisitor

> **Core goal:** A player should build *a character*, not finish a checklist. Skill points are intentionally scarce, specialization happens around the middle of the main campaign, and important choices continue through late and endgame.

---

## 1. System Overview

### 1.1 Class and subclass identity

- **Base class** is determined by equipped armor: Warrior, Mage, Archer, or Priest.
- A **complete armor set** grants the class's built-in passive bonuses, its unique armor-set bonus, and the established **+50% multiplier to that class's associated damage category**. The class bonus is *not* granted again by subclass selection.
- **Subclass** is unlocked via the main storyline around **level 25**, approximately halfway through the intended 1–50 campaign. Once a subclass is unlocked, it is selected at a class trainer, World Tree service, or Pocket Hub; its favored weapon type must be equipped for its subclass-specific effects to activate.
- Each subclass offers **four passive abilities and four active abilities**, of which players can **equip exactly one passive and one active**. The other six are alternate loadout options, not bonuses that can stack.
- The two subclass branches are mutually exclusive while active. Changing weapons alone does not automatically give free unlocks or the other branch's benefits.
- Class-specific skill-point allocations are saved independently, but all use the same level-derived budget (no fourfold accumulation of spendable points).

### 1.2 Revised unlock timeline

| Player level | Story pacing | Unlock |
|---|---|---|
| 1 | Tutorial village | Class identity from starter armor; basic attacks and class passives |
| 2–14 | Forest / First City | Shared **Tier I** nodes |
| 15–24 | Mountain Pass / World Tree | Shared **Tier II** nodes; build foundational strengths |
| **25** | **Midgame specialization quest** | **Choose one subclass** and open its branch |
| **27** | Shortly after specialization | Equip **one subclass passive** (choose among four) |
| **30** | Capital / pre-dragon progression | Equip **one subclass active** (choose among four) |
| 31–34 | Late Act I | Shared **Tier III** nodes and subclass **Tier III** nodes |
| **35** | Late-game threshold | Unlock subclass **Tier IV** nodes |
| **40** | Act II / corrupted world | Unlock subclass **Tier V** nodes and endgame upgrades |
| **45** | Deep Act II | Unlock **Tier VI: Ascension** nodes (new) |
| **50** | Endgame cap | Final point and build refinement; not full tree completion |

**Story integration:** The World Tree can offer the specialization quest after the player has completed the Frost Yeti encounter and proved readiness for the Capital. The class passive becomes available after a short subclass trial; the active after a second trial or mastery challenge. If those story events do not align with level 25/27/30, require **both** the level and the quest milestone, and avoid delaying the player's ability unlock much beyond those levels.

### 1.3 Skill points and affordability

- **Level cap: 50.** Award **1 Skill Point (SP) per level from levels 2–50**: **49 total SP** at level 50. No farmable infinite SP from enemies, quests, or consumables.
- Each **ranked upgrade node** has 1 purchase rank unless otherwise stated; a purchased node grants the effect in its row.
- Ability *slots* unlock from quest/level milestones (passive at 27, active at 30). **Selecting a passive or active costs 0 SP**; they are loadout choices. Their supporting upgrade nodes cost SP.
- Subclass unlock is free. Buying one branch never buys or activates the alternate branch.
- **Respec:** Small Gold fee at the World Tree or Pocket Hub. Out-of-combat loadout changes only. Respec refunds spent SP, not levels or quest progress.

| Tier | Minimum level | SP per node | Purpose |
|---|---:|---:|---|
| Shared I | 2 | 1 | Basic combat fundamentals |
| Shared II | 15 | 2 | Builds begin to diverge |
| Shared III | 30 | 3 | Significant late-game synergies |
| Subclass II | 25 | 2 | Early specialization |
| Subclass III | 30 | 3 | Core subclass mechanics |
| Subclass IV | 35 | 4 | Stronger late-game choices |
| Subclass V | 40 | 5 | Endgame specialization |
| Subclass VI — Ascension | 45 | 6 | Powerful exclusive endgame nodes |

**Point scarcity check (per class):** Shared tree: 4×1 + 4×2 + 4×3 = **24 SP**. A single subclass branch: 2×2 + 2×3 + 2×4 + 2×5 + 2×6 = **40 SP**. Thus a single shared tree plus **only one** subclass costs **64 SP**, **15 more** than the entire level-50 budget of 49 SP. Even without the other subclass, full completion is impossible. Completing all three branches would cost **104 SP**. This guarantees permanent tradeoffs even at level 50 (subject to respec).

**Example valid level-50 allocation:** Shared I: 3 nodes (3 SP); Shared II: 3 nodes (6 SP); Shared III: 2 nodes (6 SP); Subclass II: 2 (4 SP); Subclass III: 2 (6 SP); Subclass IV: 2 (8 SP); Subclass V: 2 (10 SP); Subclass VI: 1 (6 SP) = **49 SP**. This leaves multiple shared nodes and one Ascension option unpurchased.

### 1.4 Prerequisite rules

1. **Shared Tier I:** available starting at level 2.
2. **Shared Tier II:** level 15 and at least **2 purchased Shared I** nodes.
3. **Shared Tier III:** level 30 and at least **2 purchased Shared II** nodes.
4. **Subclass Tier II:** level 25, chosen subclass, completed specialization quest.
5. **Subclass Tier III:** level 30 and at least **1 purchased Subclass II** node.
6. **Subclass Tier IV:** level 35 and at least **1 purchased Subclass III** node.
7. **Subclass Tier V:** level 40 and at least **1 purchased Subclass IV** node.
8. **Subclass Tier VI / Ascension:** level 45 and at least **1 purchased Subclass V** node. **Choose only one of the two Ascension nodes**, even if SP would permit both.
9. Advanced shared nodes and subclass nodes are independent beyond the requirements above; the player may favor shared stats or specialized mechanics.
10. A passive or active is an equipped ability, not a tree node. Players may swap among the four offerings within the chosen subclass at safe locations after the corresponding slot is unlocked.

### 1.5 Ability slots and controls

- One **passive slot** (unlocked level 27) and one **active slot** (unlocked level 30).
- All four offered passives/actives are selectable at the given milestone, and each is viable without purchasing specific nodes. Nodes may enhance builds that pair with them but never make a choice mandatory.
- Active mapped to a dedicated configurable key (suggested **R**) rather than replacing normal left/right-click weapon behavior.
- Switching subclass or armor during combat is disabled or imposes an encounter lockout, preventing cooldown circumvention.
- Ability cooldown state persists through equipment swapping; swapping cannot reset a cooldown.

---

## 2. Warrior

**Class fantasy:** Close-range physical fighter specializing in stamina, dodges, parries, and weapon impact.  
**Base Warrior bonuses (from armor):** +10% movement speed; +5% passive melee defense reduction (interpretation to finalize: proposed as 5% enemy melee armor penetration rather than personal damage reduction).  
**Subclass weapons:** Duelist — longsword, rapier, dagger; Juggernaut — warhammer, battleaxe, heavy two-handed weapons.

### 2.1 Shared Warrior upgrades

| Tier | Node | Effect |
|---|---|---|
| I | Endurance Training | +8% maximum stamina |
| I | Combat Footwork | Dodges cost 8% less stamina |
| I | Tempered Steel | +3% melee damage |
| I | Guard Discipline | Blocking consumes 10% less stamina |
| II | Second Wind | Stamina regenerates 15% faster while below 30% stamina |
| II | Adrenaline | Taking a hit grants +10% movement speed for 2 seconds; 10s internal cooldown |
| II | Precision Strikes | Weak-point attacks deal +8% damage |
| II | Hardened Armor | +5% physical damage resistance |
| III | Battle Rhythm | Landing a melee hit restores 2 stamina; at most once per second |
| III | Veteran's Instinct | Perfectly timed dodges restore 8% maximum stamina |
| III | Combat Recovery | After avoiding damage for 5 seconds, gain +15% stamina regeneration |
| III | Last Stand | Below 25% HP, gain 12% damage resistance for 5 seconds; 30s internal cooldown |

### 2.2 Duelist — speed, precision, parries

**Favored weapons:** Longsword, rapier, dagger.  
**Identity:** Counterattack windows, consecutive hits, precision and positioning.

#### Duelist upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Swift Hands | +5% attack speed with light melee weapons |
| II | Evasive Fighter | Successful dodges grant +5% damage for 3 seconds |
| III | Deflection | Successful parries restore 10% maximum stamina |
| III | Exploit Opening | Attacking a recovering enemy deals +10% damage |
| IV | Blade Dance | Every fourth consecutive hit grants +10% movement speed for 3 seconds |
| IV | Unrelenting Assault | Successful melee hits extend combo-duration windows by 0.3s, up to +0.9s |
| V | Perfect Form | Successful parries reduce equipped active's remaining cooldown by 2s; once per 5s |
| V | Master of Blades | Critical strikes restore 5% maximum stamina and grant +5% attack speed for 3s |
| VI | **Dance of Steel** | A perfect dodge makes the next direct melee hit within 3s apply **Exposed** (+10% direct melee damage received for 3s); once per 12s. Does not stack with itself |
| VI | **Master Riposteur** | After a successful parry, the next melee strike within 3s deals an extra 35% weapon damage (once per parry); boss damage bonus capped at 20% |

#### Duelist passive choices — equip **ONE** at level 27

1. **Riposte:** A successful parry empowers the next melee strike with **+30% damage**.
2. **Blade Momentum:** Consecutive strikes increase attack speed by **2% per hit**, up to **+10%**; direct damage resets stacks.
3. **Opportunist:** Attacks against enemies during their recovery animation deal **+15% damage**.
4. **Perfect Edge:** Every third consecutive successful hit deals **+20% additional weapon damage**; missing resets the sequence.

#### Duelist active choices — equip **ONE** at level 30

1. **Flurry of Blades** — Five rapid hits dealing **200% combined weapon damage**; **14s cooldown**.
2. **Phantom Step** — Dash through an enemy with **0.3s invulnerability**; next strike deals **+35% damage**; **12s cooldown**.
3. **Blade Counter** — Enter a **0.8s parry stance**. A successful counter deals **175% weapon damage**; **16s cooldown**.
4. **Thousand Cuts** — Mark a target for **6s**. Successful hits add Wound stacks; reactivate to detonate for **15% weapon damage per stack**, max **8 stacks**; **22s cooldown**.

### 2.3 Juggernaut — impact, stagger, fortitude

**Favored weapons:** Warhammer, battleaxe, heavy two-handed weapons.  
**Identity:** Deliberate strikes, damage absorption while committed, and breaking enemy posture.

#### Juggernaut upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Heavy Grip | +8% heavy-weapon attack damage |
| II | Iron Body | +8% knockback resistance |
| III | Bonebreaker | Heavy strikes deal +15% stagger buildup |
| III | Brutal Followthrough | Heavy attacks recover 10% faster after hitting an enemy |
| IV | Unyielding | While charging a heavy attack, take 12% less damage |
| IV | Quake Impact | Staggering an enemy deals 20% weapon damage to nearby enemies within 3 blocks; 5s cooldown |
| V | Colossus | Heavy attacks gain +15% additional stagger buildup against bosses |
| V | Relentless Force | After a fully charged heavy strike, next heavy attack costs 25% less stamina |
| VI | **Mountain's Will** | Once every 20s, a heavy attack that would be interrupted instead finishes with 25% damage reduction until its recovery ends |
| VI | **Worldbreaker** | A fully charged heavy attack against a staggered enemy deals +25% direct damage and creates a 2-block shockwave (40% weapon damage); 15s cooldown; reduced vs bosses |

#### Juggernaut passive choices — equip **ONE** at level 27

1. **Crushing Force:** Heavy attacks deal **+20% stagger buildup**.
2. **Unstoppable:** During heavy-attack windups, gain **15% damage resistance** and strong knockback resistance.
3. **Executioner:** Heavy attacks deal **+18% damage** against enemies below **25% HP**.
4. **Aftershock:** Every third successful heavy attack emits a **30%-weapon-damage** shockwave within **3 blocks**.

#### Juggernaut active choices — equip **ONE** at level 30

1. **Earthshatter** — Ground slam deals **160% weapon damage** and heavy stagger in a **5-block cone**; **18s cooldown**.
2. **Titan's Wrath** — Charge an overhead strike dealing up to **275% weapon damage**; consumes **25% maximum stamina**; **20s cooldown**.
3. **Iron Bastion** — Gain **35% damage resistance** and immunity to knockback for **5s**, but move **40% slower**; **30s cooldown**.
4. **Seismic Leap** — Leap up to **10 blocks** and slam down, dealing **140% weapon damage** in an area; **18s cooldown**.

---

## 3. Mage

**Class fantasy:** Mana economy, elemental spells, spell combinations, and control.  
**Base Mage bonuses:** -5% mana cost and +40% mana regeneration from the full class armor set.  
**Subclass weapons:** Battlemage — staves and offensive spellbooks; Arcanist — wands, crystals, orbs, utility spellbooks.

### 3.1 Shared Mage upgrades

| Tier | Node | Effect |
|---|---|---|
| I | Mana Reservoir | +8% maximum mana |
| I | Arcane Meditation | +5% passive mana regeneration |
| I | Focused Casting | +3% magic damage |
| I | Quick Incantation | +5% spellcasting speed |
| II | Arcane Recovery | After 4s without casting, mana regeneration increases 15% |
| II | Mana Conservation | Utility spells cost 10% less mana |
| II | Elemental Familiarity | Elemental status effects inflicted by spells last 10% longer |
| II | Spell Stabilization | Taking damage during casting incurs 10% less interruption buildup |
| III | Arcane Replenishment | Defeating an enemy with a spell restores 4% max mana; 3s cooldown |
| III | Efficient Channeling | Channeled spells consume 8% less mana |
| III | Magical Awareness | Reveal damage type of enemies that hit you |
| III | Emergency Reserve | Once every 30s, casting below 15% mana reduces spell mana cost by 40% |

### 3.2 Battlemage — elemental destruction

**Favored weapons:** Magic staves, offensive spellbooks.  
**Identity:** Big elemental effects, area damage, mana-intensive offensive spells.

#### Battlemage upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Destructive Magic | +6% staff spell damage |
| II | Elemental Reach | AoE spells gain +8% radius |
| III | Lingering Flames | Applied elemental damage-over-time effects last 15% longer |
| III | Arcane Detonation | Spells hitting at least 3 enemies refund 5% of mana cost |
| IV | Elemental Saturation | Repeated hits with the same element build status effects 10% faster |
| IV | Spell Amplification | Fully charged spells deal +12% damage but cost 10% more mana |
| V | Elemental Mastery | Triggering an elemental status reaction restores 3% max mana; 5s cooldown |
| V | Cataclysm | Once every 20s, an AoE spell hitting 3+ enemies deals +15% damage |
| VI | **Primal Convergence** | Casting 3 distinct elements within 12s makes the next damaging spell apply 20% more status buildup; 20s cooldown |
| VI | **Mana Furnace** | When below 25% mana, the next offensive spell costs 30% less and deals +10% damage; 25s cooldown |

#### Battlemage passive choices — equip **ONE** at level 27

1. **Elemental Resonance:** Repeated spells of the same element give **+3% damage per cast**, up to **+15%**; switching element resets stacks.
2. **Mana Surge:** Spending **100 mana** empowers your next spell to deal **+20% damage**.
3. **Volatile Casting:** Offensive spells have **15% chance** to create a secondary explosion dealing **25% spell damage**.
4. **Arcane Hunger:** Defeating an enemy with magic restores **6% max mana**, at most once every **3s**.

#### Battlemage active choices — equip **ONE** at level 30

1. **Meteorfall** — After 2s, summon a meteor dealing **280% spell damage** in a **5-block radius**; **35s cooldown**.
2. **Arcane Nova** — Close-range explosion dealing **140% spell damage** and knockback; **15s cooldown**.
3. **Elemental Tempest** — Summon elemental projectiles dealing **240% total spell damage** over **6s**; **32s cooldown**.
4. **Overchannel** — For **8s**, spells gain **+30% damage** but offensive spells cost **60% more mana**; **35s cooldown**.

### 3.3 Arcanist — mastery, utility, combinations

**Favored weapons:** Wands, crystals, orbs, utility spellbooks.  
**Identity:** Spell diversity, enemy analysis, magical control, and resource efficiency.

#### Arcanist upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Wand Proficiency | Wand spells consume 6% less mana |
| II | Crystal Handling | +1 crystal carrying capacity |
| III | Rune Expertise | Runes and trap spells deal +10% damage |
| III | Arcane Insight | Orbs reveal enemy resistances and vulnerabilities |
| IV | Dual Casting | Alternating between two wand spells grants next cast +8% damage |
| IV | Crystal Resonance | Activating a crystal restores 5% max mana; 10s cooldown |
| V | Grand Arcanum | Applying 2 different magic statuses to a target reduces its magic resistance by 8% for 5s |
| V | Runic Mastery | Runes and traps persist 25% longer, without increasing maximum active count |
| VI | **Grand Equation** | Alternating 3 different spell effects within 10s grants the next wand spell 20% lower mana cost and +10% damage; 18s cooldown |
| VI | **Nullscript** | Once every 25s, successfully interrupting or absorbing an enemy spell grants an 8% max-HP shield for 5s; capped and nonstacking |

#### Arcanist passive choices — equip **ONE** at level 27

1. **Arcane Efficiency:** Wand spells consume **12% less mana**.
2. **Spellweaver:** Casting a different spell than the preceding spell grants **+15% damage** to the new spell.
3. **Weakness Insight:** Hitting a vulnerability revealed by an Orb deals **+20% damage**.
4. **Crystal Conservation:** Single-use crystals have a **15% chance not to be consumed**.

#### Arcanist active choices — equip **ONE** at level 30

1. **Spell Echo** — Your next eligible spell repeats after **1s** at **50% power**, no extra mana cost; **22s cooldown**.
2. **Arcane Reversal** — For **2s**, absorb one magical projectile and restore **12% max mana**; **20s cooldown**.
3. **Runic Seal** — Place a trap rune detonating for **150% spell damage**; **15s cooldown**.
4. **Time Fracture** — Create a zone slowing enemies and hostile projectiles by **35%** for **5s** (less effective against bosses); **32s cooldown**.

---

## 4. Archer

**Class fantasy:** Projectile accuracy, movement, weak-point damage, status effects, and good positioning.  
**Base Archer bonus:** +3% ranged damage per 10 blocks of distance from target, max +30%.  
**Subclass weapons:** Marksman — longbows and shortbows; Skirmisher — crossbows, throwing knives, blowguns, slings.

### 4.1 Shared Archer upgrades

| Tier | Node | Effect |
|---|---|---|
| I | Steady Hands | Projectile spread reduced by 10% |
| I | Light Footed | +5% movement speed while holding ranged weapons |
| I | Sharpened Ammunition | +3% physical ranged damage |
| I | Efficient Draw | Bow draw time reduced by 5% |
| II | Eagle Vision | Weak points highlighted while aiming |
| II | Nimble Recovery | Dodging after a ranged attack costs 10% less stamina |
| II | Ammunition Recovery | 10% chance to recover standard ammunition after a successful hit |
| II | Swift Reload | Crossbow reload time reduced by 8% |
| III | Tactical Retreat | Dodging away from an enemy grants +10% movement speed for 2s; 8s cooldown |
| III | Weak Point Mastery | Weak-point hits deal +10% damage |
| III | Efficient Hunter | Landing 3 consecutive ranged hits restores 5% max stamina; 6s cooldown |
| III | Ranged Survival | After damage from an enemy within 5 blocks, gain 8% damage resistance for 3s; 15s cooldown |

### 4.2 Marksman — range and precision

**Favored weapons:** Longbows, shortbows.  
**Identity:** Charging, accuracy, single-target burst, weak-point hunting.

#### Marksman upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Longshot | Projectile gravity reduced by 10% |
| II | Patient Aim | Stationary aiming for 1.5s grants +8% projectile damage |
| III | Armor Piercer | Fully drawn arrows ignore 8% of target armor |
| III | Steady Breathing | Aiming movement penalty reduced by 20% |
| IV | Precision Mastery | Consecutive weak-point hits gain +3% per hit, up to +9% |
| IV | Eagle's Reach | Fully drawn arrows travel 15% faster |
| V | Perfect Shot | Weak-point hit after 2s aiming deals additional 15% damage; 8s cooldown |
| V | Hunter's Instinct | Weak-point hit reduces subclass active remaining cooldown by 1s; 4s cooldown |
| VI | **True Sight** | First fully drawn weak-point hit on a fresh target applies **Marked Opening**: +8% damage from your next 2 arrows; 18s cooldown |
| VI | **Final Arrow** | After 3 consecutive weak-point hits, next fully drawn shot deals +25% direct damage; must be fired within 5s; 20s cooldown |

#### Marksman passive choices — equip **ONE** at level 27

1. **Deadeye:** Consecutive hits on the same target grant **+3% damage per hit**, up to **+15%**; missing resets stacks.
2. **Eagle Eye:** Projectiles travel **20% faster** and experience **20% less gravity**.
3. **Vital Strike:** Fully drawn bow shots have a **15% chance** to deal **+50% extra damage**.
4. **Predator's Focus:** Remain stationary while aiming for **2s** to get **+12% damage** on the next shot.

#### Marksman active choices — equip **ONE** at level 30

1. **Piercing Shot** — Arrow deals **175% weapon damage**, pierces enemies, ignores **25% armor**; **16s cooldown**.
2. **Rain of Arrows** — Volley deals up to **200% weapon damage** over **5s** to enemies remaining in its area; **28s cooldown**.
3. **Hunter's Mark** — Mark one target for **10s**; deal **+15% damage** against that target; only one active mark; **25s cooldown**.
4. **Execution Shot** — Channel up to **2.5s** for a sniper arrow dealing **250% weapon damage**; vulnerable while aiming; **28s cooldown**.

### 4.3 Skirmisher — movement, disruption, status

**Favored weapons:** Crossbows, throwing knives, blowguns, slings.  
**Identity:** Close/mid-range pressure, poison, quick repositioning, and hit-and-run combat.

#### Skirmisher upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Quick Hands | Reloading and throwing-weapon attack speed +8% |
| II | Lightweight Gear | Dodging costs 8% less stamina |
| III | Venom Expertise | Applied poison lasts 15% longer |
| III | Combat Acrobatics | Ranged attacks within 2s of dodging deal +8% damage |
| IV | Toxic Accumulation | Repeated poison applications increase poison buildup by 5%, up to +15% |
| IV | Guerrilla Warfare | Hits from a target's side or rear deal +10% damage |
| V | Master Saboteur | Applying a status slows movement 8% for 3s; bosses resist |
| V | Relentless Pursuit | Dodging after hitting a status-afflicted target restores 6% max stamina; 5s cooldown |
| VI | **Ghostrunner** | After using a mobility active or perfect dodge, next ranged hit within 4s applies 20% extra status buildup; 15s cooldown |
| VI | **Venom Catalyst** | Applying poison to a target already suffering another status deals an additional 20% weapon damage (not a new hit); 8s cooldown per target |

#### Skirmisher passive choices — equip **ONE** at level 27

1. **Hit and Run:** Landing a ranged attack grants **+12% movement speed** for **3s**; **6s internal cooldown**.
2. **Toxic Precision:** Attacking poisoned or bleeding enemies deals **+12% damage**.
3. **Quick Hands:** Crossbow reload and throwing-weapon attack speed increase **15%**.
4. **Escape Artist:** Dodging immediately after a ranged attack refunds **35% of the dodge's stamina cost**.

#### Skirmisher active choices — equip **ONE** at level 30

1. **Venom Barrage** — Rapid poisoned volley dealing **150% combined weapon damage** and applying poison buildup; **20s cooldown**.
2. **Smoke Bomb** — Smoke cloud lasts **5s**, disrupting normal enemy targeting and reducing ranged accuracy; **25s cooldown**.
3. **Grappling Bolt** — Tether reaches **15 blocks**, pulling the player toward terrain or around an enemy; **16s cooldown**.
4. **Trickshot** — Ricocheting projectile hits up to **5 targets** for decreasing damage and applies weapon statuses; **18s cooldown**.

---

## 5. Priest

**Class fantasy:** Holy damage, healing, defensive support, and converting offense into sustain.  
**Base Priest bonuses:** +20% incoming healing; 2× base damage vs Demons and Undead (subject to late-game balance checks).  
**Subclass weapons:** Hierophant — chimes, holy symbols, bells, healing catalysts; Inquisitor — scythes, sacrificial daggers, offensive catalysts, holy fire.

### 5.1 Shared Priest upgrades

| Tier | Node | Effect |
|---|---|---|
| I | Sacred Knowledge | +5% healing power |
| I | Divine Focus | +5% maximum mana |
| I | Blessed Casting | +3% Holy damage |
| I | Spiritual Resilience | +5% resistance to magical damage |
| II | Benevolent Touch | Healing spells cost 8% less mana |
| II | Lightbearer | Holy attacks build purification effects 10% faster |
| II | Compassion | Healing allies below 30% HP grants +8% healing effectiveness |
| II | Prayer of Renewal | Healing an ally restores 2% max mana; 6s cooldown |
| III | Sacred Bond | Healing an ally also heals lowest-health nearby ally for 8% of original amount; once per cast |
| III | Divine Endurance | After casting healing spell, gain 8% damage reduction for 3s |
| III | Faithful Resolve | Holy spells vs Undead/Demons restore 2% max mana on hit; 5s cooldown |
| III | Last Prayer | Below 20% HP, gain 15% healing effectiveness for 5s; 40s cooldown |

### 5.2 Hierophant — healing, sanctuary, protection

**Favored weapons:** Chimes, holy symbols, bells, healing catalysts.  
**Identity:** Keeping a party alive, mitigating hits, safe-zone creation, and anti-undead control.

#### Hierophant upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Gentle Grace | Direct healing spells heal 6% more |
| II | Protective Light | Healing an ally grants shield worth 4% their max HP for 3s; 8s cooldown per target |
| III | Sacred Ground | Consecrated areas last 10% longer |
| III | Guiding Prayer | Healing spells targeting allies below 50% HP cost 8% less mana |
| IV | Guardian's Blessing | Healing ally grants +5% damage resistance for 4s; nonstacking |
| IV | Purifying Touch | Direct heals reduce duration of one non-boss harmful status by 20% |
| V | Eternal Sanctuary | Healing from consecrated ground +12% effectiveness |
| V | Divine Providence | Once every 30s, healing an ally below 20% HP shields them for 10% max HP |
| VI | **Beacon of Hope** | Once every 30s, the first direct heal on an ally below 30% HP restores an extra 5% max HP and cleanses one minor debuff |
| VI | **Hallowed Refuge** | Allies who remain inside a consecrated zone for 3s gain an 8% max-HP shield lasting 4s after leaving; 20s cooldown per ally |

#### Hierophant passive choices — equip **ONE** at level 27

1. **Divine Grace:** Healing ally grants **8% damage reduction** for **5s**; nonstacking.
2. **Overflowing Light:** Excess healing creates a shield at **35% efficiency**, capped at **12% of target max HP**.
3. **Sacred Presence:** Allies within **10 blocks** receive **+10% healing from your spells**.
4. **Martyr's Blessing:** Once every **50s**, an ally within **12 blocks** falling below **25% HP** receives shield equal to **15% max HP**.

#### Hierophant active choices — equip **ONE** at level 30

1. **Sanctuary** — Holy ground for **8s**, periodically healing allies and damaging Undead; healing capped at **20% max HP per ally per cast**; **32s cooldown**.
2. **Divine Intervention** — Shield ally for up to **30% max HP** for **5s**; **35s cooldown**.
3. **Judgment Bell** — Holy wave heals allies for **12% max HP** and damages nearby Undead/Demons; **25s cooldown**.
4. **Resurrection Light** — **3s channel** to revive downed ally with **35% HP**; **120s cooldown**. **Requires an optional downed-player/revival system; otherwise substitute a rescue shield**.

### 5.3 Inquisitor — offensive healing, sacrifice, curses

**Favored weapons:** Scythes, sacrificial daggers, offensive catalysts, holy fire.  
**Identity:** Damage converted to party healing, self-sacrifice, and risk-reward offense.

#### Inquisitor upgrade nodes

| Tier | Node | Effect |
|---|---|---|
| II | Dark Communion | Life-draining abilities restore 5% more health |
| II | Ritual Blade | +5% damage with scythes and sacrificial daggers |
| III | Blood Rituals | Sacrificial abilities cost 10% less HP (without lowering damage) |
| III | Cruel Mercy | Offensive-ability healing directed to allies is 10% stronger |
| IV | Tormented Soul | +8% damage to enemies suffering holy curse or life-drain |
| IV | Harvest Essence | Killing life-drained enemy restores 3% max mana; 5s cooldown |
| V | Blood Covenant | Once every 20s, damage-to-healing conversion also gives shield equal to 4% max HP |
| V | Final Judgment | Killing cursed enemy spreads weakened curse to 1 nearby target; 10s cooldown |
| VI | **Sanguine Pact** | Once every 25s, spending HP on a sacrificial skill creates a 6% max-HP shield on lowest-health nearby ally; does not affect caster |
| VI | **Last Absolution** | A cursed enemy defeated by your direct damage sends a heal equal to 5% max HP to two lowest-health allies; 15s cooldown |

#### Inquisitor passive choices — equip **ONE** at level 27

1. **Blood Tithe:** Life-draining abilities heal player or nearby injured ally for **12% of eligible damage dealt**.
2. **Sinner's Brand:** Three hits on same enemy mark it **5s**, granting **+12% damage** from your Holy/Evil attacks.
3. **Final Rites:** Killing a life-drained enemy heals nearby allies for **5% max HP**; **8s cooldown**.
4. **Pain and Penance:** While below **50% HP**, gain **+15% damage** and **+15% life-drain effectiveness**.

#### Inquisitor active choices — equip **ONE** at level 30

1. **Blood Sacrifice** — Spend **15% current HP** to empower next spell for **+60% damage** and increased life-drain; **22s cooldown**.
2. **Reaper's Covenant** — For **8s**, weapon attacks generate healing equal to **12% eligible damage**, shared among injured allies; **32s cooldown**.
3. **Chains of Judgment** — Bind enemies in a **5-block area** for up to **4s**, dealing DoT and generating healing; **28s cooldown**.
4. **Excommunication** — Curse enemy for **6s**, then detonate based on eligible damage during curse; boss damage capped; **32s cooldown**.

---

## 6. Balancing and Combat Rules

### 6.1 Intended role differences

| Subclass | Specialty | Weakness / tradeoff |
|---|---|---|
| Duelist | Precise parries and sustained dueling | Vulnerable to AoE and stagger-resistant bosses |
| Juggernaut | Close-range stagger and committed hits | Slow windups; mobility and ranged enemies |
| Battlemage | Strong area magic and burst | High mana consumption; interruption risk |
| Arcanist | Utility, enemy analysis, spell combinations | Lower raw spell damage |
| Marksman | Distant single-target precision | Pressure from enemies in melee range |
| Skirmisher | Mobility, status effects and disruption | Lower long-range boss burst |
| Hierophant | Protecting and healing the group | Limited offensive output |
| Inquisitor | Healing through aggressive pressure | Risky against bosses where attacking is unsafe |

### 6.2 Stacking and proc rules

- **Percent damage bonuses of the same type are additive within a damage bucket**; do not multiply every node, passive, weapon effect, and armor-set bonus separately. Keep the established +50% full-set class multiplier as one distinct, clearly documented multiplier.
- **Skill-derived attack-speed bonus cap:** +25%.
- **Skill-derived general damage resistance cap:** 35%, with short defensive actives handled as explicit exceptions, never invulnerability chains.
- **Cooldown reductions from skills:** at most 30% of an active's original cooldown per use; no cooldown can be reduced below a sensible absolute minimum.
- **DoT, reflected damage, chained hits, item procs and life-steal healing cannot recursively trigger their own origin effects.** A single spell cast or attack event can yield at most one designated restoration/proc event per effect and internal cooldown.
- **Bosses resist hard crowd control.** Replace knockdown with stagger buildup and cap slows; no stun-lock loops.
- **Life-steal uses actual eligible damage** after defenses, excluding overkill and most secondary procs. Put per-target/per-activation healing caps on AoE siphoning. Do not let one Inquisitor heal a full party to full from a single crowded AoE.
- **Critical damage, headshots/weak points and damage amplification** need explicit caps and test cases against the Archer's already-strong distance passive.
- **Defensive shield stacking:** largest shield from a given category applies, or use a global shield cap; do not indefinitely pile shields on one target.
- **Consumables and crystal interactions:** crystal conservation never duplicates key items, boss-drop materials or currency; only explicitly eligible single-use spell crystals.
- **Switching equipment during combat** cannot clear ability cooldowns, remove an applied debuff or activate multiple subclasses.
- **Mana and stamina regeneration from repeated hits** must use internal cooldowns to avoid scaling infinitely with fast weapons or multitarget spells.
- **Active attacks with high listed % damage** are a total cast coefficient (not repeated full coefficients per hit). Area coefficients are tuned separately from single-target coefficients.

### 6.3 How to measure balance

1. Benchmark time-to-kill on a training dummy at levels 15, 25, 30, 40 and 50 with comparable gear.
2. Test sustained DPS and 10-second burst separately, including real mana/stamina constraints.
3. Test 1-player, 2-player and 4-player boss fights. Include ranged-only and mixed compositions.
4. Test early Act II enemies and the Frost Yeti rematch with several viable builds, including a budget support build.
5. Verify that any one subclass's best boss DPS does not vastly exceed similarly specialized alternatives *unless* it has a clear survivability or range tradeoff.
6. Test support healing-per-second, effective shield uptime and survival impact instead of comparing support by raw DPS.
7. Rebalance first using cooldowns, costs, hit requirements and caps, then only change core damage ratios as needed.

---

## 7. Recommended Starter Loadouts

| Subclass | Recommended passive | Recommended active | Combat loop |
|---|---|---|---|
| Duelist | Riposte | Blade Counter | Parry → counterattack |
| Juggernaut | Crushing Force | Earthshatter | Build stagger → break enemy posture |
| Battlemage | Elemental Resonance | Overchannel | Repeated elemental attacks → burst window |
| Arcanist | Spellweaver | Spell Echo | Alternate spells → duplicate key spell |
| Marksman | Deadeye | Piercing Shot | Land consecutive arrows → precision burst |
| Skirmisher | Toxic Precision | Grappling Bolt | Apply poison → reposition → exploit status |
| Hierophant | Overflowing Light | Sanctuary | Sustain group → prevent incoming damage |
| Inquisitor | Blood Tithe | Reaper's Covenant | Deal damage → distribute healing |

These are *suggested* loadouts, not forced builds. No choice should be strictly superior in every encounter.

## 8. Implementation Notes and Open Questions

- **UI:** Full-screen class skill tree with a central shared branch, a subclass split, locked/unlocked indicators, connector lines, SP counter, tier requirements and hover previews. Show active ability key and cooldown.
- **Persistence:** Save per-class purchased nodes, specialization state, current subclass, passive/active selections and active cooldowns. Server-authoritative validation is essential for multiplayer.
- **Source of truth:** Every bonus should be represented by a clear stat modifier or effect handler; the UI should read the same values the combat calculation uses.
- **Story gates:** Confirm exactly which narrative quest grants specialization and whether the Act II transition occurs around level 40. Levels and quests should not deadlock each other.
- **Prerequisite visual connections:** Current tier rules are deliberately simple. Individual node-to-node connector prerequisites can be designed after playtesting to avoid accidental mandatory filler nodes.
- **Unresolved mechanical definitions:** Warrior's +5% melee defense reduction wording; whether priest's 2× anti-undead damage applies to every attack or only Holy attacks; the status-effect catalog; exact dodge/parry timings; downed-player system; mana and stamina baseline values.
- **Further expansion:** If the game later raises the level cap, add new high-tier nodes and a separate limited currency rather than allowing players to purchase all remaining nodes automatically.

---

## 9. Skill Tree Content Totals

| Content | Per class | All 4 classes |
|---|---:|---:|
| Shared upgrade nodes | 12 | 48 |
| Subclass upgrade nodes (10 per branch × 2 branches) | 20 | 80 |
| Total upgrade nodes shown | **32** | **128** |
| Subclass passive choices | 8 | 32 |
| Subclass active choices | 8 | 32 |
| Equippable subclass passives at a time | 1 | 1 for current class |
| Equippable subclass actives at a time | 1 | 1 for current class |
| Level-50 SP budget | 49 | Same 49-SP budget per active class build |

**Final design guarantee:** Even limiting attention to a single subclass branch, fully purchasing the shared class tree and that branch takes **64 SP**, but a level-50 player receives only **49 SP**. The two Ascension nodes are additionally exclusive. Players must prioritize their strengths throughout midgame, late game and endgame.

