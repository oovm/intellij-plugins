# Fluent IntelliJ monorepo

Multi-plugin workspace for JetBrains Marketplace plugins and one shared library.

| Path | Role |
|------|------|
| [`projects/plugins/intellij-fluent`](projects/plugins/intellij-fluent) | Fluent language plugin (`README.md` / `CHANGELOG.md` live here) |
| [`projects/packages`](projects/packages) | Shared code-only library (`:packages`) |
| [`projects/designs`](projects/designs) | Brand assets |

```bash
./gradlew runIde          # load every :plugins/* together
./gradlew buildPlugins    # separate zip per plugin
./gradlew ciVerify
```

Each plugin module owns its Marketplace text: the whole `README.md` is the description, and `CHANGELOG.md` is the release notes. The repo-root README is monorepo navigation only.
