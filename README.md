# Fluent IntelliJ monorepo

Multi-plugin workspace for JetBrains Marketplace plugins and one shared library.

| Path | Role |
|------|------|
| [`projects/plugins/intellij-fluent`](projects/plugins/intellij-fluent) | Fluent language plugin (`README.md` / `CHANGELOG.md` live here) |
| [`projects/plugins/intellij-jss`](projects/plugins/intellij-jss) | JSS language plugin (`.jss`) |
| [`projects/plugins/intellij-wit`](projects/plugins/intellij-wit) | WIT IDL plugin (`.wit` / `.witx`) |
| [`projects/plugins/intellij-yggdrasil`](projects/plugins/intellij-yggdrasil) | Yggdrasil language plugin (`.ygg` / `.yggdrasil`) |
| [`projects/packages`](projects/packages) | Shared code-only library (`:packages`) |
| [`projects/designs`](projects/designs) | Brand assets and ecosystem manifests (`ecosystems/`, `jss/`, …) |

```bash
./gradlew runIde          # load every :plugins/* together
./gradlew buildPlugins    # zip per plugin, collected in build/
./gradlew ciVerify
```

Each plugin module owns its Marketplace text: the whole `README.md` is the description, and `CHANGELOG.md` is the release notes. The repo-root README is monorepo navigation only.
