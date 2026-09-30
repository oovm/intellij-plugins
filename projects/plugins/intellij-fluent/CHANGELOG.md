<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Fluent Intellij Changelog

## [Unreleased]
### Changed
- Move Fluent language implementation into `packages/fluent` with `fluent.*` layer layout
- Split `intellij-fluent` plugin descriptors under `META-INF/languages` and `META-INF/actions`
- Keep Vue and Markdown integrations in `fluent.plugin.integration`

## [0.4.7]
### Added
- Structure view support ([#7](https://github.com/oovm/intellij-plugins/issues/7))
- Vue SFC `<fluent>` block injection and optional Markdown `ftl`/`fluent` fence highlighting ([#10](https://github.com/oovm/intellij-plugins/issues/10))
### Fixed
- Wrong format indent level of selection ([#5](https://github.com/oovm/intellij-plugins/issues/5))
- Extra indentation in Vue SFC injected Fluent fragments ([#11](https://github.com/oovm/intellij-plugins/issues/11))
- Stop bundling `:packages` test helpers in the plugin distribution
- Replace internal `OptionsBundle` color labels with `FluentBundle` keys
- Use `AbstractBundle` instead of deprecated `DynamicBundle(String)`

## [0.4.6]
### Added
- Replace the `Flex` parser with a handwritten parser
- Change the line-based parser to a segment-based parser
### Removed
- Drop `Flex` support

## [0.4.3]
### Fixed
- Fix missing api incompatible for (`261.*`)

## [0.4.2]
### Fixed
- Fix missing api incompatible for (`251.*`)

## [0.4.1]
### Fixed
- Fix missing api incompatible for (`241.*`)

## [0.4.0]
### Added
- Support highlight block in markdown ([#10](https://github.com/oovm/fluent-intellij/issues/10))
- Support highlight template in vue ([#10](https://github.com/oovm/fluent-intellij/issues/10)).
### Removed
- Drop 2020.3 support

## [0.3.2]
### Fixed
- Fix missing api incompatible for (`223.*`) ([#9](https://github.com/oovm/fluent-intellij/issues/9))
- Fix wrong highlight in code schema demo.

## [0.3.1]
### Fixed
- Fix missing api incompatible for (`203.*`)

## [0.3.0]
### Added
- Support structure view ([#7](https://github.com/oovm/fluent-intellij/issues/7))
### Fixed
- Fix attributes in placeable ([#8](https://github.com/oovm/fluent-intellij/issues/8))


## [0.2.3]
### Change
- Update to 2022 version (`222.*`)

## [0.2.2]
### Change
- Update to 2022 version (`221.*`)

## [0.2.1]
### Fixed
- Fix syntax highlight of string escaping

## [0.2.0]
### Fixed
- Fix syntax highlight of string escaping

## [0.1.0]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
