# Changelog

## [0.2.0] - 2026-09-30

### Changed

- Migrated from standalone `yggdrasil-intellij` into the `intellij-plugins` monorepo
- Language sources under `packages/yggdrasil.*`, thin plugin shell with `xi:include` descriptors
- Replace internal `OptionsBundle` color labels with `YggdrasilBundle` keys
- Use `AbstractBundle` instead of deprecated `DynamicBundle(String)`
- Declare `com.intellij.modules.lsp` for LSP integration
- Remove unresolved optional plugin dependencies on `org.rust.lang` and `com.jetbrains.ultimate`
- Migrate LSP registration to `LspIntegrationProvider` / `ProjectWideLspClientDescriptor`
- Use non-deprecated `CreateFileFromTemplateAction` and `LineMarkerInfo` constructors
