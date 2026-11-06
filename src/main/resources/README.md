# Runtime resources

`assets/tentativerpg/` holds original client assets. `data/tentativerpg/` holds server datapack content. The namespace is provisional; confirm it before publishing stable IDs.

Reserved assets: `blockstates`, `models/block`, `models/item`, `textures/block`, `textures/item`, `textures/entity`, `textures/gui`, `lang`, `sounds`.
Reserved vanilla data: `recipe`, `loot_table`, `tags/item`, `tags/block`, `damage_type`, `advancement`.

These are directory reservations, not loaded content. Validate version-specific formats against the pinned build when adding files. Custom skills, quests or regions need explicit schemas, loaders, reload validation and synchronization before custom resource directories are added. Animation-library-specific paths follow the selected API after its POC. `META-INF/` is reserved for metadata supplied by the verified MDK; no loader metadata is fabricated here.
