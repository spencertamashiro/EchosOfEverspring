# Development areas and team handoffs

This is a proposed ownership map for parallel human development, not an assignment of people. Spencer and Dylan can claim a workstream per issue; use one primary owner and a reviewer from the adjoining area. All gameplay remains in one mod JAR.

| Workstream | Main paths | Owns | Coordinate with |
|---|---|---|---|
| Platform and state | `src/main/java/dev/tentativerpg/{registry,player,networking,commands}/`, future root build files | Bootstrap, persistence, migrations, packet contracts | Every gameplay owner; client for sync |
| Character progression | `src/main/java/dev/tentativerpg/{progression,classsystem,skills,stats}/` | Class identity, SP, unlocks, stat snapshots | Combat, equipment, UI |
| Combat and abilities | `src/main/java/dev/tentativerpg/{combat,abilities,magic}/` | Damage, resources, cooldowns, casting | Progression, encounters, client |
| Items and economy | `src/main/java/dev/tentativerpg/{equipment,items,economy}/` | Equipment, shops, atomic transactions | Combat, progression, story |
| Campaign and travel | `src/main/java/dev/tentativerpg/{story,travel,map,world}/` | Story authority, region state, teleport permissions | World authors, client, economy |
| Encounters and bosses | `src/main/java/dev/tentativerpg/{encounters,bosses}/` | Boss phases, threat and encounter lifecycle | Combat, campaign, world authors |
| Client and art | `src/main/java/dev/tentativerpg/client/`, `src/main/resources/assets/`, `art/` | Screens, input, presentation and original assets | Packet and gameplay contract owners |
| Integrations | `src/main/java/dev/tentativerpg/compat/`, `docs/compat-tests/` | Isolated adapters and compatibility evidence | Platform and affected gameplay owner |
| QA and release | `src/test/`, `docs/testing/` | Test evidence and mod release checks | All affected owners |

## Shared contracts

Before parallel implementation, agree on stable namespaced IDs, persistence version, immutable stat inputs/outputs, transaction idempotency keys and versioned packet shapes in a scoped ADR. Proposals remain proposals until reviewed. Server services own authoritative changes; UI requests intent and renders synced views. Avoid cyclic feature dependencies by defining small contracts at the owning feature boundary. Do not create a generic dumping-ground utility package.

Coordinate changes to registrations, packet registration, build files, shared language files and schemas through their owner. Feature branches should include the relevant logic, resources, tests and docs together. No person owns a folder permanently; an issue records current ownership and integration needs.

## Practical split for two contributors

One contributor can take the first Warrior server slice (state, armor identity, stats and unlock validation); the other can independently prepare UI wireframes/original art or review gameplay design and test cases. Agree on IDs and read-only view contracts before UI binding. Both review the first build and dedicated-server smoke test. These are suggested assignments only.
