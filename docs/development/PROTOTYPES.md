# NPC and tooltip prototypes

TentativeRPG 0.1.0 combines the NPC dialogue and quest prototype from `RPGtestingNPC` with the standalone `tooltips-only` module from `tooltipRPGtesting`. The bundled NPCs and quests are sample content for development worlds.

## Build and try it

Use Java 21 with Minecraft 1.21.1 and NeoForge 21.1.265. From the repository root, run `./gradlew test build`, `./gradlew runClient`, or `./gradlew runGameTestServer`. On Windows, use `.\gradlew.bat` in place of `./gradlew`. Development runs use separate directories below `run/`.

- Run `/rpgtestingnpc demo` on clear ground with operator permission, then right-click a resident. See the [NPC authoring guide](NPC_AUTHORING.md) for custom residents, quest assignment, and voice packs.
- Try the example `/give` command in the [tooltip attribute guide](TOOLTIP_ATTRIBUTES.md). Tagged items show a painted card and expose readable tooltip text to narration. The client also renders the authored rose and thorn corner ornaments. Container panels use Minecraft's own textures.

## Dependencies

The Gradle runs resolve these dependencies from the publishers. For an external game instance, install matching runtime dependencies alongside the TentativeRPG JAR.

| Dependency | Pinned version | Environment | License |
| --- | --- | --- | --- |
| [NeoForge](https://maven.neoforged.net/releases/net/neoforged/neoforge/21.1.265/) | 21.1.265 | Client and server | LGPL-2.1 |
| [Easy NPC Core](https://modrinth.com/mod/easy-npc-core/version/9TVwS7rU) | 7.18.0, file `9TVwS7rU` | Client and server | MIT code, separate asset terms |
| [FTB Quests](https://maven.ftb.dev/releases/dev/ftb/mods/ftb-quests-neoforge/2101.1.36/) | 2101.1.36 | Client and server | All Rights Reserved |
| [FTB Library](https://maven.ftb.dev/releases/dev/ftb/mods/ftb-library-neoforge/2101.1.36/) | 2101.1.36 | Client and server | All Rights Reserved |
| [FTB Teams](https://maven.ftb.dev/releases/dev/ftb/mods/ftb-teams-neoforge/2101.1.9/) | 2101.1.9 | Client and server | All Rights Reserved |
| [Architectury API](https://maven.architectury.dev/dev/architectury/architectury-neoforge/13.0.8/) | 13.0.8 | Client and server | LGPL-3.0 |
| [Tooltip Overhaul](https://modrinth.com/mod/tooltip-overhaul/version/j00qCZqC) | 2.1.0, file `j00qCZqC` | Client | GPL-3.0 code, separate asset terms |

[ModDevGradle 2.0.148](https://github.com/neoforged/ModDevGradle) and the Gradle 9.2.1 wrapper provide the build. [JUnit Jupiter 5.11.4](https://github.com/junit-team/junit5/tree/r5.11.4) and Gson 2.10.1 are test dependencies. The JAR includes the MIT notices for imported Tooltip RPG code and [PixelFX animation atlases](https://github.com/patch-gg/pixelfx/tree/a84159688af3ca37a4be05a0d422b94451b2e24e). External dependency JARs resolve separately.

## Prototype data

Replace the standalone RPGtestingNPC and Tooltip RPG - Tooltips JARs with TentativeRPG on both ends of a multiplayer connection. Keep Tooltip Overhaul on the client. It retains the prototype's NPC tags, server config paths, FTB chapter and task IDs, voice resource locations, and tooltip item data key. It uses TentativeRPG packet IDs, so use matching JAR versions on both ends. The NPC system can spawn residents in response to operator commands and initializes the bundled FTB chapter files in the instance config.
