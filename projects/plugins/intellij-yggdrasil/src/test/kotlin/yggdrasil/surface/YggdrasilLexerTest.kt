package yggdrasil.surface

import com.intellij.lexer.Lexer
import com.intellij.openapi.util.text.StringUtil
import com.intellij.testFramework.LexerTestCase
import java.nio.file.Files
import java.nio.file.Path
import yggdrasil.surface.psi.YggdrasilLexer

class YggdrasilLexerTest : LexerTestCase() {
    override fun getDirPath(): String = "lexer"

    override fun createLexer(): Lexer = YggdrasilLexer()

    private fun doLexerFileTest(fileName: String) {
        val testDataDir = Path.of("src", "test", "testData", "lexer")
        val input = StringUtil.convertLineSeparators(Files.readString(testDataDir.resolve(fileName)))
        val expectedPath = testDataDir.resolve(fileName.removeSuffix(".yggdrasil") + ".txt")
        if (java.lang.Boolean.getBoolean("regenerate")) {
            Files.writeString(expectedPath, printTokens(input, 0))
            return
        }
        val expected = StringUtil.convertLineSeparators(Files.readString(expectedPath))
        doTest(input, expected)
    }

    fun testWhitespace() {
        doLexerFileTest("whitespace.yggdrasil")
    }

    fun testComments() {
        doLexerFileTest("comments.yggdrasil")
    }

    fun testStringLiterals() {
        doLexerFileTest("string-literals.yggdrasil")
    }

    fun testNumberLiterals() {
        doLexerFileTest("number-literals.yggdrasil")
    }
}
