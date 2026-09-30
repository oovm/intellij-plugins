package wit.language

import wit.surface.psi.WitLexer
import com.intellij.lexer.Lexer
import com.intellij.openapi.util.text.StringUtil
import com.intellij.testFramework.LexerTestCase
import java.nio.file.Files
import java.nio.file.Path

class WitLexerTest : LexerTestCase() {
    override fun getDirPath(): String = "lexer"

    override fun createLexer(): Lexer = WitLexer()

    private fun doLexerFileTest(fileName: String) {
        val testDataDir = Path.of("src", "test", "testData", "lexer")
        val input = StringUtil.convertLineSeparators(Files.readString(testDataDir.resolve(fileName)))
        val expectedPath = testDataDir.resolve(fileName.removeSuffix(".wit") + ".txt")
        if (java.lang.Boolean.getBoolean("regenerate")) {
            Files.writeString(expectedPath, printTokens(input, 0))
            return
        }
        val expected = StringUtil.convertLineSeparators(Files.readString(expectedPath))
        doTest(input, expected)
    }

    fun testBasic() {
        doLexerFileTest("basic.wit")
    }

    fun testExperimental() {
        doLexerFileTest("experimental.wit")
    }

    fun testLexerPositionRestore() {
        val input = StringUtil.convertLineSeparators(
            """
            package docs:example;

            interface greet {
              greet: func(name: string) -> string;
            }
            """.trimIndent()
        )
        val lexer = createLexer()
        lexer.start(input)

        val resumeOffset = input.indexOf("interface")
        var resumeState = 0
        while (lexer.tokenType != null) {
            if (lexer.tokenEnd == resumeOffset) {
                resumeState = lexer.state
                break
            }
            lexer.advance()
        }

        val resumedLexer = createLexer()
        resumedLexer.start(input, resumeOffset, input.length, resumeState)

        assertEquals("interface", input.substring(resumedLexer.tokenStart, resumedLexer.tokenEnd))
    }
}
