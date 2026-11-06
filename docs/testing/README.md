# Test planning and evidence

No automated test suite, Gradle build, client boot or server boot exists yet.

| Layer | Future location/evidence | Required coverage |
|---|---|---|
| Pure unit tests | `src/test/java/`, `src/test/resources/` | 49-SP scarcity, prerequisites/exclusivity, armor identity, modifier caps, proc guards, idempotent transactions |
| Loader integration | Source set selected by actual MDK | Registration, serialization, resource validation and optional-mod absence |
| Multiplayer | Dated reports in this directory | Two isolated players, reconnect, death, dimension changes, restart and migrations |
| Dedicated server | Dated reports in this directory | Headless startup without client classes; server authority and malformed request rejection |
| Third-party POC | `../compat-tests/` | Exact pinned group, client/server boot, event interactions, permission evidence |
| Mod release | Dated reports in this directory | Mod build, upgrade and save preservation checks |

Record command, pinned versions, date, environment, expected behavior, observed result and evidence path. Do not commit private logs or local game worlds. Failed and not-run checks remain visible; a checklist is not a passing test.
