package jss.test

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.LightPlatformTestCase
import jss.test.TestTimeout
import jss.surface.lexer.JssLexer
import jss.surface.psi.JssTypes

class JssLexerSmokeTest : LightPlatformTestCase() {
    fun testKeywordsLiteralsAndPunctuation() = TestTimeout.run {
        assertTokens(
            "let x = 1",
            listOf(
                JssTypes.KW_LET,
                TokenType.WHITE_SPACE,
                JssTypes.SYMBOL,
                TokenType.WHITE_SPACE,
                JssTypes.EQ,
                TokenType.WHITE_SPACE,
                JssTypes.INTEGER,
            ),
        )
    }

    fun testClassAndBraces() = TestTimeout.run {
        assertTokens(
            "class Foo {}",
            listOf(
                JssTypes.KW_CLASS,
                TokenType.WHITE_SPACE,
                JssTypes.SYMBOL,
                TokenType.WHITE_SPACE,
                JssTypes.BRACE_L,
                JssTypes.BRACE_R,
            ),
        )
    }

    fun testLineComment() = TestTimeout.run {
        // Line-comment lexer consumes the trailing newline, so `KW_LET` follows immediately.
        assertTokens(
            "// note\nlet y = 2",
            listOf(
                JssTypes.COMMENT,
                JssTypes.KW_LET,
                TokenType.WHITE_SPACE,
                JssTypes.SYMBOL,
                TokenType.WHITE_SPACE,
                JssTypes.EQ,
                TokenType.WHITE_SPACE,
                JssTypes.INTEGER,
            ),
        )
    }

    private fun assertTokens(text: String, expected: List<IElementType>) {
        val lexer = JssLexer()
        lexer.start(text)
        val actual = mutableListOf<IElementType>()
        while (lexer.tokenType != null) {
            actual.add(lexer.tokenType!!)
            lexer.advance()
        }
        assertEquals(expected, actual)
    }
}
