# Prototype compatibility checks

| Check | Observed result |
| --- | --- |
| Gradle `test build` | Build succeeded. Two tooltip metadata tests passed. |
| NeoForge `runGameTestServer` with Easy NPC Core and FTB Quests | Two required GameTests passed. Tests exercised repeated demo placement, custom NPC spawn and binding, dialogue payload serialization and FTB quest progression. Tooltip Overhaul was absent from the server mod list. |
| NeoForge `runClient` with Tooltip Overhaul 2.1.0 | TentativeRPG and Tooltip Overhaul loaded. Resource and audio initialization completed. FTB Library reported missing icon settings for some Easy NPC entities. |
| Combined skill, NPC and tooltip build | Two tooltip unit tests passed. All three GameTests passed, including skill prices, tier prerequisites, subclass and Ascension exclusions, and the Weapon Skills overlay. |
| Combined client quick-play | Both skill packs loaded and the player joined the isolated save. The client window stayed open after adding the texture iteration compatibility fix. |
| Spell Engine 1.9.8 melee regression | The isolated combined client joined a copied world. The integrated server stayed running after receiving a Swipe attack-effects packet without Better Combat. |
| Manual client interaction and live multiplayer | Pending. |

These checks ran in isolated development instances. The [integration guide](development/PROTOTYPES.md) records the dependency versions and launch commands.
