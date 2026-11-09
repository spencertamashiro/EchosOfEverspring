# Mod source layout

The root Gradle project builds one mod JAR with ID `tentativerpg`.

- `main/java/dev/tentativerpg/npc/` owns server-authoritative NPC dialogue and prototype quests.
- `main/java/dev/tentativerpg/tooltip/` owns item metadata used by the renderer.
- `main/java/dev/tentativerpg/client/` owns physical-client UI and sound.
- `main/resources/` contains loader metadata, authored content and asset notices.
- `test/java/` contains tooltip unit tests. `SmokeGameTests` and its template are available to the Gradle GameTest server.

See [the prototype integration guide](../docs/development/PROTOTYPES.md) for startup commands and legacy IDs.
