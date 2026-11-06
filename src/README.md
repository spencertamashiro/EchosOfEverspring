# Mod source layout

One root Gradle project will produce one gameplay mod JAR. No Gradle project or source-set wiring exists yet.

- `main/java/dev/tentativerpg/`: feature packages; `client/` is the physical-client boundary.
- `main/resources/`: authored runtime assets, datapack content and eventual loader metadata.
- `test/java/`: pure logic tests, mirroring production packages once code exists.
- `test/resources/`: small deterministic test fixtures.
- `generated/resources/`: reserved for datagen output; wire it explicitly in the selected MDK before use.

`dev.tentativerpg` and resource namespace `tentativerpg` are provisional filesystem names chosen for this scaffold, not approved registry identities. Confirm package ownership and stable mod ID in a platform ADR before adding code or metadata, then rename these folders together if needed.

Tests requiring Minecraft boot belong in the source set supported by the pinned MDK, chosen when that MDK is imported. Do not assume a GameTest source set or task already exists. Add Java classes only as real vertical slices.
