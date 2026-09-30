<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# JSS Intellij Changelog

## [Unreleased]
### Changed
- Move JSS language implementation from mistaken `vos.*` package to `packages/jss`
- Register `intellij-jss` for `language=JSS` and `.jss` only, with `JssBundle` resources

## [0.2.2]
### Changed
- Absorb JSS language support from valkyrie-intellij: `jss.*` in `:packages`, thin `intellij-jss` plugin shell
### Fixed
- Bundle `:packages` under plugin `lib/` so `jss.*` classes load at runtime (fixes ClassNotFoundException on Marketplace)
- Drop unused protobuf workspace protocol stubs to shrink the plugin zip
- Stop using internal `LexerPositionImpl` and `UtilsKt` offset helpers in JSS lexing/folding
- Parse `object` type symbols and `property`/`properties` declarations without breaking the file ([#139](https://github.com/oovm/intellij-plugins/issues/139))
- Offer schema property completions based on enclosing object or array type ([#138](https://github.com/oovm/intellij-plugins/issues/138))
- Keep test helpers out of the packaged `:packages` library
- Replace internal `OptionsBundle` color labels with `JssBundle` keys
- Use `AbstractBundle` instead of deprecated `DynamicBundle(String)`
