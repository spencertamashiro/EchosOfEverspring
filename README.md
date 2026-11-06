# tentativeRPG

Development repository for one custom NeoForge RPG mod. A modpack may follow later and is outside the current scope. A dedicated server implementation is eventually planned, so all gameplay, networking, and progression systems are designed for headless dedicated server compatibility and multiplayer scalability.

## Documentation

- [Agent and development guidelines](AGENTS.md)
- [Claude Code & AI agent guide](CLAUDE.md)
- [Project updates & changelog](UPDATES.md)
- [Development layout and team workstreams](docs/development/README.md)
- [Contribution workflow](CONTRIBUTING.md)
- [Game design manuscript (Act I & II storyline and systems)](docs/GAME_DESIGN.md)
- [Skill tree design and combat progression](docs/SKILL_TREES.md)
- [Third-party mod compatibility research](docs/MOD_COMPATIBILITY_RESEARCH.md)

Development areas are separated into `src/` for the mod, `art/` for original assets, and `docs/` for design and team guidance. See the development guide for ownership boundaries.

## Current status

This repository contains design documentation and a development directory scaffold. No Java implementation or Gradle build is included. Minecraft 1.21.1, NeoForge 21.1.x, and Java 21 are a suggested baseline; exact build versions remain unpinned.

Compatibility and licensing statements are preserved from the supplied research snapshot and have not been independently reverified as part of this import. No dependencies have been installed and no gameplay or multiplayer tests have been run.
