# Handcrafted campaign world

- `manifests/`: reviewed map versions, dimension/region/POI identifiers, required block/mod IDs and export checksums.
- `schematics/act-i/` and `schematics/act-ii/`: deliberately selected original schematic sources for the two hand-authored states.
- `exports/`: export instructions and release artifact references; archives are ignored by default.

Keep editable saves and verified backups outside Git. Work on copies only; never infer POI coordinates or generate campaign terrain. Coordinates come from actual authored saves. A dimension/topology ADR is still required before runtime wiring. Record exact block dependencies when third-party blocks are used. Large exports need an explicit release-artifact or LFS plan.
