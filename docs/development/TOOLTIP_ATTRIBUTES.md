# Writing tooltip attributes (Minecraft 1.21.1)

TentativeRPG reads a JSON string at `minecraft:custom_data.tooltiprpg` on vanilla or modded item stacks. Install Tooltip Overhaul 2.1.0 on clients as described in the [integration guide](PROTOTYPES.md).

## Choose the appearance

| Attribute | Determines |
| --- | --- |
| `rarity` | Border and rarity text color; MYTHIC uses the larger bold crimson frame; other rarities use compact 180px cards. LEGENDARY is gold. |
| `style` | Animated PixelFX background and its accent/ornaments. `rarity` gives a quiet rarity card. A Mythic must select one of the 20 signature IDs below. |
| `role` | Class label and bottom signet: sword (WARRIOR), arcane star (MAGE), bow (ARCHER), cross (PRIEST). |
| `type` | Type label and stat layout. ARMOR displays armor; every other type displays damage and critical chance. |
| Actual item stack | The textured, rotating top icon. Changing `type` does not replace the real item's model. |

The type, class, rarity, and style are independent: a legendary mage longsword can use the `astral` background. Selecting an element or utility does not automatically select a style. Font is Minecraft's default; non-Mythic cards keep native-size text.

## Complete definition

Supply the following fields when attaching raw item data.

```json
{
  "id": "ashen_promise",
  "name": "Ashen Promise",
  "type": "LONGSWORD",
  "rarity": "MYTHIC",
  "role": "WARRIOR",
  "element": "Fire",
  "damage": 28,
  "crit": 20,
  "stamina": 10,
  "mana": 0,
  "armor": 0,
  "utility": "FIRE",
  "lore": "A promise tempered beyond the last dawn.",
  "style": "cinder"
}
```

| Field | Accepted values / bounds |
| --- | --- |
| `id` | 1–48 lowercase letters, digits, underscores; presentation identity, not registry ID |
| `name` | 1–48 printable characters |
| `type` | LONGSWORD, DAGGER, WARHAMMER, BATTLEAXE, BOW, CROSSBOW, STAFF, WAND, CATALYST, CHIME, SCYTHE, ARMOR |
| `rarity` | COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, MYTHIC |
| `role` | WARRIOR, MAGE, ARCHER, PRIEST |
| `element` | 1–28 letters, spaces, slash, or hyphen; visual affinity label |
| `damage` | Finite number, 0–500 |
| `crit` | Finite number, 0–100 percent |
| `stamina`, `mana` | Integers, 0–100; zero hides the row |
| `armor` | Integer, 0–20; displayed for ARMOR |
| `utility` | NONE, FIRE, POISON, FROST, LIFESTEAL, SHOCK, HEAL; NONE hides the utility section |
| `lore` | 0–240 printable characters |
| `style` | `rarity`, or a signature ID below; case-sensitive lowercase |

Signature IDs: `cinder`, `glacier`, `tempest`, `eclipse`, `seraph`, `venom`, `tide`, `astral`, `bloom`, `bloodmoon`, `meteor`, `sandstorm`, `aurora`, `clockwork`, `soulfire`, `thunderhead`, `crystal`, `rift`, `dragon`, `requiem`.

The JSON string may be at most 4096 characters. Unknown keys, invalid enum spelling, malformed JSON, and out-of-range values cause the ordinary tooltip to remain in place. Invalid metadata is not executed. Enum values use uppercase; styles use lowercase. Item names, lore, and IDs are plain text.

## Paste directly into a creative world

This command requires cheats/operator permission. Run this in a TentativeRPG development instance:

```mcfunction
/give @s minecraft:diamond_sword[minecraft:custom_data={tooltiprpg:'{"id":"ashen_promise","name":"Ashen Promise","type":"LONGSWORD","rarity":"MYTHIC","role":"WARRIOR","element":"Fire","damage":28,"crit":20,"stamina":10,"mana":0,"armor":0,"utility":"FIRE","lore":"A promise tempered beyond the last dawn.","style":"cinder"}'}]
```

For a compact gold card, change `rarity` to `LEGENDARY` and `style` to `rarity`. For a frost staff use `type: STAFF`, `role: MAGE`, `element: Frost`, and `style: glacier`. These examples describe changes inside the JSON string, not SNBT keys outside it.

Functions, loot tables, plugins, or server code can supply the same `minecraft:custom_data` component. Preserve the JSON string layer: an NBT compound named `tooltiprpg` will not be read as a string.

## Java integration

```java
Gear definition = GearData.parseGear(json); // validates and throws on invalid input
GearData.attach(stack, definition);         // preserves unrelated custom-data keys
Gear attached = GearData.read(stack);       // returns null if missing/invalid
```

API classes are `dev.tentativerpg.tooltip.Gear` and `dev.tentativerpg.tooltip.GearData`. These classes are part of the TentativeRPG JAR. A server that only attaches serialized metadata does not need the Java API.

## Display metadata versus gameplay attributes

These fields control tooltip presentation. `damage: 28` does not set a sword's real attack damage, `FIRE` does not ignite a target, and resource costs are labels. Your gameplay mod/server must implement those rules and keep the metadata consistent. Use Minecraft's `minecraft:attribute_modifiers` for actual vanilla attack/armor stats, and your own gameplay code for critical rolls and utilities.

Metadata does not change durability, enchantments, sounds, item model, attack speed, class requirements, or item mechanics. In particular, tagging a vanilla sword with `type: STAFF` changes its tooltip layout and label but does not turn it into a casting item.
