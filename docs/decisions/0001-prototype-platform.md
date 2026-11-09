# 0001. Platform for the NPC and tooltip integration

**Status** Proposed as the baseline for the integrated prototypes.

Both local prototypes use Minecraft 1.21.1, NeoForge 21.1.265, Java 21, ModDevGradle 2.0.148 and Gradle 9.2.1. Use those versions and the repository scaffold's `dev.tentativerpg` package and `tentativerpg` mod ID so both features load from one JAR. Keeping separate mod IDs would preserve independent prototype releases at the cost of two mod lifecycles and a separate deployment path.

Use the `tentativerpg` namespace for new payloads and assets. Preserve the NPC prototype's authored config, sound locations, binding tags and FTB task identity alongside the `tooltiprpg` item metadata key. Existing content can keep those identifiers when switching to the integrated mod. Keep client rendering and voice playback behind physical-client entry points. The [integration guide](../development/PROTOTYPES.md) records pinned dependencies and test commands.
