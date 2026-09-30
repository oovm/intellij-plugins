# intellij-yggdrasil

IntelliJ plugin for [Yggdrasil](https://github.com/ygg-lang) (`.ygg` / `.yggdrasil`).

Language implementation lives in `:packages` under the `yggdrasil.*` package; this module is the Marketplace plugin shell.

## Features

- Hand-written lexer and parser (no Grammar-Kit / ANTLR generation)
- Syntax highlighting, structure view, formatting, and completion
- Optional LSP integration when Ultimate is present
