# Installer and release staging

Reserve this area for reviewed launcher-import and opt-in world-import tooling. No executable installer or scripts exist yet, despite planned filenames referenced in older guidance.

Before implementation, write `docs/INSTALLER_AND_ENV.md` and `docs/LEGAL_AND_RELEASE.md`, define the canonical environment contract from AGENTS.md, and choose approved release payloads. Use app-owned staging, validate archive containment, never overwrite a save silently and preserve saves on upgrade/uninstall. Do not build an independent third-party JAR downloader.
