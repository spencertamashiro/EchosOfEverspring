# Dedicated server operations

- `config/`: reviewed shareable configuration templates without credentials or private addresses.
- `deployment/`: versioned startup/deployment instructions after an actual server build is available.
- `operations/`: backup, restore, monitoring and upgrade runbooks with tested evidence.

Server gameplay implementation belongs in the same mod under `src/main/java/`, not in a second mod or this folder. Live instances, worlds, logs, accounts, EULA acceptance and secrets stay outside the repo. No server executable, startup script or automatic EULA acceptance is provided by this scaffold.
