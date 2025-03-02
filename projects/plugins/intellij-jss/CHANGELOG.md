<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# JSS Intellij Changelog

## [Unreleased]

## [0.2.2]
### Changed
- Absorb JSS language support from valkyrie-intellij: `vos.*` in `:packages`, thin `intellij-jss` plugin shell
### Fixed
- Bundle `:packages` under plugin `lib/` so `vos.*` classes load at runtime (fixes ClassNotFoundException on Marketplace)
- Keep test helpers out of the packaged `:packages` library
- Replace internal `OptionsBundle` color labels with `VosBundle` keys
- Use `AbstractBundle` instead of deprecated `DynamicBundle(String)`
