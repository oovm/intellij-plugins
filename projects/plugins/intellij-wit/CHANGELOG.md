# Changelog

## [Unreleased]

## [0.1.4]
### Added
- Initial rewrite: lexer, parser, syntax highlighting, brace matcher, commenter
- Experimental `@refine` / `@predicate` attribute hints ([#98](https://github.com/oovm/intellij-plugins/issues/98))
- Experimental `extend … with { … }` syntax ([#99](https://github.com/oovm/intellij-plugins/issues/99))
### Fixed
- Stop bundling `:packages` test helpers in the plugin distribution
- Replace internal `OptionsBundle` color labels with `WitBundle` keys
- Use `AbstractBundle` instead of deprecated `DynamicBundle(String)`
