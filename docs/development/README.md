# Development guide

- [Team workstreams and handoffs](TEAM_WORK.md)
- [Contribution workflow](../../CONTRIBUTING.md)
- [Source layout](../../src/README.md)
- [Decisions](../decisions/README.md)
- [Testing](../testing/README.md)

Before importing the MDK: confirm exact Minecraft/NeoForge/Java toolchain versions, mod ID, Java package, licensing and build plugin versions in a reviewed platform ADR. The suggested baseline remains Minecraft 1.21.1 / NeoForge 21.1.x / Java 21; no patch is pinned here.

Next executable slice: matching MDK, one mod entry point, then Warrior armor identity, a deterministic damage/stat snapshot and server-validated skill state with pure tests. Prove dedicated-server boot before expanding systems.
