package vos.test

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.LightPlatformTestCase
import com.intellij.testFramework.TestTimeout
import vos.surface.lexer.VosLexer
import vos.surface.psi.VosTypes

class VosLexerSmokeTest : LightPlatformTestCase() {
    fun testKeywordsLiteralsAndPunctuation() = TestTimeout.run {
        assertTokens(
            "let x = 1",
            listOf(
                VosTypes.KW_LET,
                TokenType.WHITE_SPACE,
                VosTypes.SYMBOL,
                TokenType.WHITE_SPACE,
                VosTypes.EQ,
                TokenType.WHITE_SPACE,
                VosTypes.INTEGER,
            ),
        )
    }

    fun testClassAndBraces() = TestTimeout.run {
        assertTokens(
            "class Foo {}",
            listOf(
                VosTypes.KW_CLASS,
                TokenType.WHITE_SPACE,
                VosTypes.SYMBOL,
                TokenType.WHITE_SPACE,
                VosTypes.BRACE_L,
                VosTypes.BRACE_R,
            ),
        )
    }

    fun testLineComment() = TestTimeout.run {
        // Line-comment lexer consumes the trailing newline, so `KW_LET` follows immediately.
        assertTokens(
            "// note\nlet y = 2",
            listOf(
                VosTypes.COMMENT,
                VosTypes.KW_LET,
                TokenType.WHITE_SPACE,
                VosTypes.SYMBOL,
                TokenType.WHITE_SPACE,
                VosTypes.EQ,
                TokenType.WHITE_SPACE,
                VosTypes.INTEGER,
            ),
        )
    }

    private fun assertTokens(text: String, expected: List<IElementType>) {
        val lexer = VosLexer()
        lexer.start(text)
        val actual = mutableListOf<IElementType>()
        while (lexer.tokenType != null) {
            actual.add(lexer.tokenType!!)
            lexer.advance()
        }
        assertEquals(expected, actual)
    }
}
