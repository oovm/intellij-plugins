# scripts

| Directory | Purpose |
|-----------|---------|
| `dev/` | Local verification aligned with CI (optional) |

## Repository layout

- `projects/plugins/intellij-fluent` — Fluent Marketplace plugin
- `projects/plugins/intellij-jss` — JSS Marketplace plugin (`.jss`)
- `projects/plugins/intellij-wit` — WIT IDL Marketplace plugin
- `projects/packages` — Shared library only (code, no `resources`; Gradle project `:packages`)
- `projects/designs` — Brand assets and ecosystem manifests (`ecosystems/`, `jss/`, …)

## Local CI

```bash
./gradlew ciVerify
```

## Run vs build

| Task | Behavior |
|------|----------|
| `./gradlew runIde` | Start one IDE sandbox with every `:plugins/*` module loaded |
| `./gradlew buildPlugins` | Build a separate zip per plugin (not merged) |
| `:plugins:intellij-*:publishPlugin` | Publish and verify each plugin independently |
