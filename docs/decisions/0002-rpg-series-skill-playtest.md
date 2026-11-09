# 0002. RPG Series skill-tree playtest integration

**Status** Implemented for the combined development instance.

TentativeRPG owns the Everspring tree data, textures, and rendering adjustment in its existing JAR. Pufferfish's Skills and Skill Tree (RPG Series) supply the native menu, graph, and purchase engine. The existing Everspring namespace and node IDs are kept stable. The Weapon Skills category overlay adjusts the pinned upstream layout and background and keeps its node definitions and mechanics.

Pufferfish's data schema controls connection colors and graph direction. Its client renderer couples arrowheads to direction and draws the connector in two passes. The client-only mixin targets that renderer in exactly Pufferfish 0.16.8. It suppresses arrowheads and the inner pass while leaving server-side directions untouched. The dependency lock records official artifacts and their licenses and hashes. The project distributes its own code and assets through its JAR, with third-party dependencies resolved separately.

The isolated test allocation starts each class at level-50 budget for UI and balance review. Campaign-level awarding, armor and weapon checks, and the quest milestones are separate work. The live skill test covers budget, prerequisites, exclusions and the Weapon Skills overlay. Restart the development client after each integration iteration and confirm the playtest save joins and its window stays open.

The merged client repeatedly failed immediately after joining when the Minecraft 1.21.1 texture manager iterated its tickable texture set during a modification. The client-only compatibility mixin replaces that set with a concurrent set at construction. The combined client subsequently joined and stayed open. This patch is specific to the pinned Minecraft version.
