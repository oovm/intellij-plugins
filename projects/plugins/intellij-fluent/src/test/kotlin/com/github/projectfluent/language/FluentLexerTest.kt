package com.github.projectfluent.language

import com.github.projectfluent.language.psi.FluentLexer
import com.github.projectfluent.language.psi.FluentTypes
import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase
import com.intellij.openapi.util.text.StringUtil
import java.nio.file.Files
import java.nio.file.Path

class FluentLexerTest : LexerTestCase() {
    override fun getDirPath(): String {
        return "lexer"
    }

    override fun createLexer(): Lexer {
        return FluentLexer()
    }

    private fun doLexerFileTest(fileName: String) {
        val testDataDir = Path.of("src", "test", "testData", "lexer")
        val input = StringUtil.convertLineSeparators(Files.readString(testDataDir.resolve(fileName)))
        val expectedPath = testDataDir.resolve(fileName.removeSuffix(".ftl") + ".txt")
        if (java.lang.Boolean.getBoolean("regenerate")) {
            Files.writeString(expectedPath, printTokens(input, 0))
            return
        }
        val expected = StringUtil.convertLineSeparators(Files.readString(expectedPath))
        doTest(input, expected)
    }

    fun testEmpty() {
        doLexerFileTest("empty.ftl")
    }

    fun testWhitespace() {
        doLexerFileTest("whitespace.ftl")
    }

    fun testComments() {
        doLexerFileTest("comments.ftl")
    }

    fun testSimpleMessages() {
        doLexerFileTest("simple-messages.ftl")
    }

    fun testStringLiterals() {
        doLexerFileTest("string-literals.ftl")
    }

    fun testNumberLiterals() {
        doLexerFileTest("number-literals.ftl")
    }

    fun testMessageReferences() {
        doLexerFileTest("message-references.ftl")
    }

    fun testSelectExpressions() {
        doLexerFileTest("select-expressions.ftl")
    }

    fun testFunctionCalls() {
        doLexerFileTest("function-calls.ftl")
    }

    fun testInlinePlaceables() {
        doLexerFileTest("inline-placeables.ftl")
    }

    fun testDotMessage() {
        doLexerFileTest("dot-message.ftl")
    }

    fun testLexerStateRestoreAtTopLevelMessage() {
        val input = StringUtil.convertLineSeparators(
            """
            title = This thing
            
            description = Blah blah blah.
            
            message = This is message!
            """.trimIndent()
        )
        val lexer = createLexer()
        lexer.start(input)

        val resumeOffset = input.indexOf("description")
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

        assertEquals(FluentTypes.SYMBOL, resumedLexer.tokenType)
        assertEquals("description", input.substring(resumedLexer.tokenStart, resumedLexer.tokenEnd))
    }

    fun testNestedExpressionKeepsIntegerToken() {
        val input = StringUtil.convertLineSeparators(
            """
            complex-nested =
              Hello, {if {${'$'}unreadCount} > 0 ->
                 [true] one
                *[false] none
              }
            """.trimIndent()
        )
        val lexer = createLexer()
        lexer.start(input)

        var integerCount = 0
        while (lexer.tokenType != null) {
            if (
                lexer.tokenType == FluentTypes.INTEGER &&
                input.substring(lexer.tokenStart, lexer.tokenEnd) == "0"
            ) {
                integerCount++
            }
            lexer.advance()
        }

        assertEquals(1, integerCount)
    }

    fun testStringEscapes() {
        doLexerFileTest("string-escapes.ftl")
    }
}
