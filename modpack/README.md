# Launcher-managed modpack

- `manifests/`: exact platform-approved project/file references after version and permission review.
- `profiles/client/`: client-only configuration overlays.
- `profiles/server/`: dedicated-server configuration overlays.
- `profiles/builder/`: authoring-only tools/configuration; excluded from player releases.
- `datapacks/` and `resourcepacks/`: original optional pack overrides, separate from core mod resources.

These are reserved authoring folders, not a launcher-importable pack. No dependency or manifest is selected. Define the target launcher/export format before wiring overrides. Never commit downloaded JARs or put core progression in pack scripts. Keep exact client/server required versions aligned; builder tools stay separate.
