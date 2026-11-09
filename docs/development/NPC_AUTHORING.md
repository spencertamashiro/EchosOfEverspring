# Authoring TentativeRPG characters

This guide covers the NPC dialogue feature in TentativeRPG. Use JSON to create an Easy NPC character with dialogue, optional voice clips, and an optional FTB Quests assignment. Create characters by editing JSON and restarting the game or server.

## Create and test a character

1. Copy [harbor_quartermaster.json](../../examples/npc/harbor_quartermaster.json) for a quest giver or [cliff_lookout.json](../../examples/npc/cliff_lookout.json) for a conversation-only character. Each file contains one complete definition.
2. Place the file in the Minecraft instance's `config/rpgtestingnpc/npcs/` folder. For the project's Gradle test client, the folder is `run/client/config/rpgtestingnpc/npcs/`. Use the dedicated server's folder for a multiplayer world. The mod creates this folder on startup.
3. Set a unique `id`. For a quest giver, also allocate an unused `quest` number. Choose the Easy NPC model, dialogue theme, and voice settings described below.
4. Restart the client for singleplayer or the server for multiplayer. Look for `Loaded ... custom RPGtestingNPC definitions` in `logs/latest.log`. If a file fails validation, the log identifies the file and error. Other valid definitions still load.
5. With cheats or operator permissions, stand on a flat surface and run `/rpgtestingnpc spawn harbor_quartermaster`. Substitute the new character's ID. Step back and right-click. Tab completion lists the IDs available on the server.

To use a character you already styled in Easy NPC, stand near it and bind the dialogue. Pick a selector that resolves to exactly one Easy NPC. The binding preserves the existing NPC's appearance, behavior, and world name.

```mcfunction
/rpgtestingnpc bind cliff_lookout @e[type=easy_npc:humanoid,sort=nearest,limit=1,distance=..4]
```

A spawned or bound actor keeps the tag `rpgtestingnpc.<id>`. Keep `id` stable so an existing actor picks up dialogue edits after a restart. To rebind an actor, remove its old tag with `/tag <actor> remove rpgtestingnpc.<old_id>`, then bind the new ID. Configure the model or skin in Easy NPC before binding. The JSON `model` applies when the addon spawns a new actor.

## Definition fields

Use UTF-8 JSON with double quotes and one object per file. Field names are case-sensitive. Files load in filename order. The first valid file owns a duplicated ID or quest key, and the log reports later collisions. The bundled characters reserve their IDs and quest keys 1–12.

| Field | Use |
| --- | --- |
| `id` | Required. Use 1–32 lowercase letters, digits, or underscores. The filename can match it for easier maintenance. |
| `name` | Required dialogue speaker name, up to 40 characters. |
| `model` | Required Easy NPC entity path, without the namespace. Tested choices include `humanoid`, `humanoid_slim`, and `villager`. |
| `greeting` | Required opening line. |
| `loreLabel`, `lore` | Required first story option and its reply. |
| `extraLoreLabel`, `extraLore` | Optional second story option and reply for a character without an assignment. Set both together. |
| `caption` | Optional heading, up to 40 characters. Shown below the speaker name. |
| `frame` | Optional frame preset. Omit it to inherit the instance defaults. |
| `colors` | Optional object with `accent`, `border`, `background`, and `text` overrides in `#RRGGBB` format. |
| `sounds` | Optional map from dialogue line IDs to resource-pack sound events. See [Voice clips](#voice-clips). |

Dialogue lines accept up to 160 single-line characters, and option labels accept up to 32. Start with 60–80 characters for comfortable reading on a compact panel. Players can click the speech area to reveal a line immediately.

Version 1.3 definitions can keep their old `style` field during upgrade. All NPCs use the shared typewriter panel. Remove the legacy field when editing the file.

## Dialogue theme

The panel has one layout with five static frame presets. Each preset supplies a palette, corner ornaments, and a seal. Choose `ASTRAL`, `TIDE`, `CINDER`, `AURORA`, or `CLOCKWORK`.

Set the defaults for the bundled cast and custom NPCs without their own preset in `config/rpgtestingnpc-dialogue.toml`. In multiplayer, edit the server's file. For the development client, the path is `run/client/config/rpgtestingnpc-dialogue.toml`.

```toml
defaultFrame = "ASTRAL"

[defaultColors]
accent = ""
border = ""
background = ""
text = ""
```

The empty strings inherit the selected preset's colors. Use a quoted `#RRGGBB` value to override one. On a custom NPC, `frame` starts from that preset's palette and `colors` overrides individual entries. With no NPC `frame`, any NPC `colors` are applied on top of the instance defaults.

```json
"frame": "TIDE",
"colors": {
  "accent": "#87E7DD",
  "border": "#377B79",
  "background": "#102A31",
  "text": "#F3FFF9"
}
```

Restart the client or server after editing. The server sends the configured frame and colors with the dialogue. Players see the author's choice.

## Optional FTB Quests assignment

Omit the quest fields entirely for a conversation-only character. For a quest giver, add the following fields to its base definition.

| Field | Use |
| --- | --- |
| `quest` | Unique integer from 13 to 4095. Keep it stable once a world has used it. FTB progress is attached to IDs derived from this number. |
| `questTitle`, `objective` | Quest-book title and instructions, up to 80 and 200 characters respectively. Tell the player what to bring and to whom. |
| `item`, `count` | Registered item ID such as `minecraft:string` and the required quantity from 1 to 64. |
| `reward`, `rewardCount` | Optional registered item ID and quantity from 1 to 64. Include both or omit both. The player claims the reward in the FTB quest book. |
| `acceptLabel`, `accepted` | Acceptance option and its response. |
| `turnInLabel`, `finished` | Optional hand-in button label and required successful hand-in response. The label defaults to **Turn in supplies**. |
| `responses` | Optional response overrides keyed by the line IDs below. |

The addon generates **Custom NPC Assignments** in `config/ftbquests/quests/chapters/rpgtestingnpc_custom.snbt`. Change the JSON definition when you want to update this chapter. Startup regenerates the chapter from the active definitions. The generated chain is acceptance → supplies → turn-in. Accepting reveals the assignment. The NPC checks the player's carried items, consumes the requested quantity, and completes the supply and turn-in tasks. Progress follows the FTB team.

Keep a simple ledger of assigned quest keys when building a modpack. Use a new key for a different assignment so it cannot inherit another quest's saved progress. Changes to an existing assignment's requirements or reward apply to that same saved quest.

## Voice clips

Add a `sounds` map to voice any subset of the dialogue. Use an event ID with a namespace. These events are defined in `sounds.json`, which maps to mono Ogg Vorbis clips in a client resource pack. The text panel always shows the line, including when a clip is unavailable.

```json
"sounds": {
  "GREETING": "harbor:iona.greeting",
  "LORE": "harbor:iona.lore",
  "FINISHED": "harbor:iona.finished"
}
```

A resource pack for the first event can use this layout. Use your own namespace for new voices.

```text
Harbor Voices/
  pack.mcmeta
  assets/
    harbor/
      sounds.json
      lang/en_us.json
      sounds/iona/greeting.ogg
```

Example `pack.mcmeta` for Minecraft 1.21.1

```json
{"pack":{"pack_format":34,"description":"Harbor NPC voice clips"}}
```

Example `assets/harbor/sounds.json`

```json
{
  "iona.greeting": {
    "subtitle": "subtitles.harbor.iona.greeting",
    "sounds": ["harbor:iona/greeting"]
  }
}
```

Define the matching subtitle key in `assets/harbor/lang/en_us.json` if using subtitles. Enable the pack in the client's Resource Packs screen or distribute it as a server resource pack. Clients receive the dialogue text from the server. They need the audio resource pack to play custom events through the **Voice/Speech** volume channel.

| Line ID | When it plays or appears |
| --- | --- |
| `GREETING` | Right-clicking the NPC |
| `LORE`, `EXTRA_LORE` | The first and optional second story answers |
| `ACCEPTED`, `ACTIVE` | New acceptance and repeated acceptance |
| `LOCKED`, `WAITING` | Attempting to turn in before acceptance and while missing supplies |
| `FINISHED`, `REPORTED` | Successful hand-in and a repeat hand-in |
| `UNAVAILABLE` | The server cannot advance the assignment |

## Handoff checklist

- Record the new NPC ID, model, and quest key in your handoff. Include where the JSON and any resource pack live.
- Test the greeting, every option, the configured palette, and any voice events. For a quest giver, check the locked state, acceptance, insufficient supplies, a successful turn-in, and a repeat turn-in.
- Use one unique tag per actor. Use the same addon version on client and server.
- In this repository, run `.\gradlew.bat build runGameTestServer` to exercise the shipped sample roster and the two example definitions in an isolated server. The addon JAR appears in `build/libs/`.

## Implementation map

- `CustomNpcs.java` loads and validates external definitions. `Cast.java` holds the shared fields and the sample cast.
- `RPGTestingNPC.java` handles spawning, binding, and server-authorized choices. `DialogueNetwork.java` sends the current dialogue to the client.
- `CustomQuestChapter.java` generates the custom chapter. `QuestBridge.java` and `NpcSupplyTask.java` handle progress and item consumption.
- `RelicDialogueScreen.java` renders the shared typewriter panel. `RelicTheme.java` resolves the palette from settings, and `ClientHooks.java` plays optional positional audio. The bundled sample data and clips in `src/main/resources/` are working references for new definitions.
