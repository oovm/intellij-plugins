# Fluent Intellij

![](./src/main/design/SVG/title.svg)

<!-- Plugin description -->
### [Fluent Language](https://github.com/projectfluent) Support

- Syntax highlighting
- Semantic highlighting
- Structure view
- [Markdown]()/[Vue]() Support

<!-- Plugin description end -->


## Commands

常规回归：

```bash
./gradlew --no-daemon --no-configuration-cache test --tests "*FluentLexerTest*" --tests "*FluentParserTest*" --tests "*FluentHighlightTest*"
```

如果本机只有较新的 JBR/JDK，需要临时覆盖 Kotlin toolchain 版本：

```bash
./gradlew --no-daemon --no-configuration-cache test --tests "*FluentLexerTest*" --tests "*FluentParserTest*" --tests "*FluentHighlightTest*" -PkotlinToolchainVersion=25
```

如果 Gradle 没有自动识别到本机 JDK/JBR，可显式指向 `JAVA_HOME`：

macOS / Linux:

```bash
./gradlew --no-daemon --no-configuration-cache test -PkotlinToolchainVersion=25 -Dorg.gradle.java.installations.auto-detect=false -Dorg.gradle.java.installations.paths="$JAVA_HOME"
```

Windows PowerShell:

```powershell
./gradlew.bat --no-daemon --no-configuration-cache test -PkotlinToolchainVersion=25 -D"org.gradle.java.installations.auto-detect=false" -D"org.gradle.java.installations.paths=$env:JAVA_HOME"
```

重生成 lexer 基线：

macOS / Linux:

```bash
./gradlew --no-daemon --no-configuration-cache test --tests "*FluentLexerTest*" -PkotlinToolchainVersion=25 -Dregenerate=true -Dorg.gradle.java.installations.auto-detect=false -Dorg.gradle.java.installations.paths="$JAVA_HOME"
```

Windows PowerShell:

```powershell
./gradlew.bat --no-daemon --no-configuration-cache test --tests "*FluentLexerTest*" -PkotlinToolchainVersion=25 -Dregenerate=true -D"org.gradle.java.installations.auto-detect=false" -D"org.gradle.java.installations.paths=$env:JAVA_HOME"
```
