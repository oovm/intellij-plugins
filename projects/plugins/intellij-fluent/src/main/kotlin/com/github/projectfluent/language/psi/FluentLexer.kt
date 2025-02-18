package com.github.projectfluent.language.psi

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class FluentLexer : Lexer() {
    companion object {
        private const val STATE_INSIDE_STRING = 1
        private const val STATE_AT_LINE_START = 1 shl 1
        private const val STATE_EXPECT_DEFINITION_IDENTIFIER = 1 shl 2
        private const val STATE_EXPRESSION_DEPTH_SHIFT = 8
        private const val STATE_STRING_INTERPOLATION_DEPTH_SHIFT = 20
    }

    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentToken: IElementType? = null
    private var expressionDepth: Int = 0
    private var stringInterpolationDepth: Int = 0
    private var insideString: Boolean = false
    private var atLineStart: Boolean = true
    private var expectDefinitionIdentifier: Boolean = false

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentOffset = startOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        this.currentToken = null
        restoreState(resolveInitialState(initialState, startOffset))
        advance()
    }

    override fun getState(): Int {
        var state = 0
        if (insideString) {
            state = state or STATE_INSIDE_STRING
        }
        if (atLineStart) {
            state = state or STATE_AT_LINE_START
        }
        if (expectDefinitionIdentifier) {
            state = state or STATE_EXPECT_DEFINITION_IDENTIFIER
        }
        state = state or (expressionDepth shl STATE_EXPRESSION_DEPTH_SHIFT)
        state = state or (stringInterpolationDepth shl STATE_STRING_INTERPOLATION_DEPTH_SHIFT)
        return state
    }

    override fun getTokenType(): IElementType? {
        return currentToken
    }

    override fun getTokenStart(): Int {
        return tokenStart
    }

    override fun getTokenEnd(): Int {
        return tokenEnd
    }

    override fun advance() {
        do {
            tokenStart = currentOffset
            currentToken = null

            if (currentOffset >= endOffset) {
                tokenEnd = currentOffset
                return
            }

            val c = buffer[currentOffset]

            if (insideString && !isInsideStringInterpolation()) {
                consumeStringContent()
            } else {
                when {
                    c == '\n' || c == '\r' -> {
                        consumeLineEnd()
                    }
                    atLineStart && c == ' ' -> {
                        consumeIndent()
                    }
                    c == ' ' || c == '\t' -> {
                        consumeInlineBlank()
                    }
                    c == '#' -> {
                        if (atLineStart) {
                            consumeComment()
                        } else {
                            consumeTextLine()
                        }
                    }
                    c == '"' -> {
                        currentToken = FluentTypes.STRING_QUOTE
                        currentOffset++
                        insideString = true
                        atLineStart = false
                    }
                    c.isLetter() || c == '_' -> {
                        if (isInsideExpression() || atLineStart || expectDefinitionIdentifier) {
                            consumeSymbol()
                        } else {
                            consumeTextLine()
                        }
                    }
                    c.isDigit() -> {
                        if (isInsideExpression()) {
                            consumeNumberInExpression()
                        } else {
                            consumeTextLine()
                        }
                    }
                    c == '{' -> {
                        currentToken = FluentTypes.BRACE_L
                        currentOffset++
                        expressionDepth++
                        if (insideString) {
                            stringInterpolationDepth++
                        }
                        atLineStart = false
                    }
                    c == '}' -> {
                        currentToken = FluentTypes.BRACE_R
                        currentOffset++
                        if (expressionDepth > 0) {
                            expressionDepth--
                        }
                        if (insideString && stringInterpolationDepth > 0) {
                            stringInterpolationDepth--
                        }
                        atLineStart = false
                    }
                    c == '[' -> {
                        currentToken = FluentTypes.BRACKET_L
                        currentOffset++
                        atLineStart = false
                    }
                    c == ']' -> {
                        currentToken = FluentTypes.BRACKET_R
                        currentOffset++
                        atLineStart = false
                    }
                    c == '(' -> {
                        currentToken = FluentTypes.PARENTHESIS_L
                        currentOffset++
                        atLineStart = false
                    }
                    c == ')' -> {
                        currentToken = FluentTypes.PARENTHESIS_R
                        currentOffset++
                        atLineStart = false
                    }
                    c == '<' -> {
                        currentToken = FluentTypes.ANGLE_L
                        currentOffset++
                        atLineStart = false
                    }
                    c == '>' -> {
                        currentToken = FluentTypes.ANGLE_R
                        currentOffset++
                        atLineStart = false
                    }
                    c == '=' -> {
                        currentToken = FluentTypes.EQ
                        currentOffset++
                        atLineStart = false
                    }
                    c == ':' -> {
                        currentToken = FluentTypes.COLON
                        currentOffset++
                        atLineStart = false
                    }
                    c == ';' -> {
                        currentToken = FluentTypes.SEMICOLON
                        currentOffset++
                        atLineStart = false
                    }
                    c == ',' -> {
                        currentToken = FluentTypes.COMMA
                        currentOffset++
                        atLineStart = false
                    }
                    c == '$' -> {
                        currentToken = FluentTypes.DOLLAR
                        currentOffset++
                        atLineStart = false
                    }
                    c == '.' -> {
                        if (isInsideExpression() || atLineStart) {
                            currentToken = FluentTypes.DOT
                            currentOffset++
                            expectDefinitionIdentifier = atLineStart
                            atLineStart = false
                        } else {
                            consumeTextLine()
                        }
                    }
                    c == '*' -> {
                        currentToken = FluentTypes.STAR
                        currentOffset++
                        atLineStart = false
                    }
                    c == '-' -> {
                        if (isInsideExpression() || atLineStart) {
                            currentToken = FluentTypes.HYPHEN
                            currentOffset++
                            expectDefinitionIdentifier = atLineStart
                            atLineStart = false
                        } else {
                            consumeTextLine()
                        }
                    }
                    c == '`' -> {
                        currentToken = FluentTypes.ACCENT
                        currentOffset++
                        atLineStart = false
                    }
                    c == '\\' -> {
                        currentToken = FluentTypes.STRING_ESCAPE
                        currentOffset++
                        if (currentOffset < endOffset) {
                            currentOffset++
                        }
                        atLineStart = false
                    }
                    else -> {
                        consumeTextLine()
                    }
                }
            }

            tokenEnd = currentOffset
        } while (currentToken == null && currentOffset < endOffset)
    }

    private fun consumeStringContent() {
        if (currentOffset >= endOffset) {
            insideString = false
            return
        }

        val c = buffer[currentOffset]

        when {
            c == '"' -> {
                currentToken = FluentTypes.STRING_QUOTE
                currentOffset++
                insideString = false
            }
            c == '{' -> {
                currentToken = FluentTypes.BRACE_L
                currentOffset++
                expressionDepth++
                stringInterpolationDepth++
                atLineStart = false
            }
            c == '\\' -> {
                currentToken = FluentTypes.STRING_ESCAPE
                currentOffset++
                if (currentOffset < endOffset) {
                    currentOffset++
                }
            }
            else -> {
                val start = currentOffset
                while (currentOffset < endOffset) {
                    val nextC = buffer[currentOffset]
                    if (nextC == '"' || nextC == '\\' || nextC == '{') {
                        break
                    }
                    currentOffset++
                }
                if (currentOffset > start) {
                    currentToken = FluentTypes.STRING_CHAR
                }
            }
        }
    }

    private fun consumeLineEnd() {
        if (buffer[currentOffset] == '\r') {
            currentOffset++
            if (currentOffset < endOffset && buffer[currentOffset] == '\n') {
                currentOffset++
            }
        } else {
            currentOffset++
        }
        currentToken = FluentTypes.LINE_END
        atLineStart = true
        expectDefinitionIdentifier = false
    }

    private fun consumeIndent() {
        while (currentOffset < endOffset && buffer[currentOffset] == ' ') {
            currentOffset++
        }
        currentToken = FluentTypes.INDENT
        atLineStart = true
    }

    private fun consumeInlineBlank() {
        while (currentOffset < endOffset) {
            val c = buffer[currentOffset]
            if (c != ' ' && c != '\t') {
                break
            }
            currentOffset++
        }
        currentToken = FluentTypes.INLINE_BLANK
        if (expectDefinitionIdentifier) {
            expectDefinitionIdentifier = false
        }
    }

    private fun consumeComment() {
        currentOffset++
        while (currentOffset < endOffset && buffer[currentOffset] != '\n') {
            currentOffset++
        }
        currentToken = FluentTypes.COMMENT_LINE
        atLineStart = false
    }

    private fun consumeSymbol() {
        val start = currentOffset
        while (currentOffset < endOffset) {
            val c = buffer[currentOffset]
            if (!c.isLetterOrDigit() && c != '_' && c != '-') {
                break
            }
            currentOffset++
        }
        if (currentOffset > start) {
            val symbolText = buffer.substring(start, currentOffset)
            currentToken = when (symbolText) {
                "if" -> FluentTypes.IF_KEYWORD
                "true" -> FluentTypes.TRUE_KEYWORD
                "false" -> FluentTypes.FALSE_KEYWORD
                else -> FluentTypes.SYMBOL
            }
            atLineStart = false
            expectDefinitionIdentifier = false
        }
    }

    private fun consumeNumberInExpression() {
        while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
            currentOffset++
        }
        if (currentOffset < endOffset && buffer[currentOffset] == '.') {
            currentOffset++
            while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
                currentOffset++
            }
            currentToken = FluentTypes.DECIMAL
        } else {
            currentToken = FluentTypes.INTEGER
        }
        atLineStart = false
        expectDefinitionIdentifier = false
    }

    private fun consumeTextLine() {
        val start = currentOffset
        // Always consume at least one character to avoid infinite loop
        if (currentOffset < endOffset) {
            currentOffset++
            while (currentOffset < endOffset) {
                val nextC = buffer[currentOffset]
                when {
                    nextC.isWhitespace() ||
                    (nextC == '#' && atLineStart) ||
                    nextC == '"' ||
                    nextC == '{' ||
                    nextC == '}' ||
                    nextC == '[' ||
                    nextC == ']' ||
                    nextC == '(' ||
                    nextC == ')' ||
                    nextC == '<' ||
                    nextC == '>' ||
                    nextC == '=' ||
                    nextC == '$' ||
                    nextC == '`' -> {
                        break
                    }
                    else -> {
                        currentOffset++
                    }
                }
            }
            currentToken = FluentTypes.TEXT_LINE
            atLineStart = false
            expectDefinitionIdentifier = false
        }
    }
    


    override fun getCurrentPosition(): LexerPosition {
        return object : LexerPosition {
            override fun getOffset(): Int = currentOffset
            override fun getState(): Int = getState()
        }
    }

    override fun restore(position: LexerPosition) {
        currentOffset = position.offset
        tokenStart = currentOffset
        tokenEnd = currentOffset
        currentToken = null
        restoreState(position.state)
        advance()
    }

    private fun restoreState(state: Int) {
        insideString = state and STATE_INSIDE_STRING != 0
        atLineStart = state and STATE_AT_LINE_START != 0
        expectDefinitionIdentifier = state and STATE_EXPECT_DEFINITION_IDENTIFIER != 0
        expressionDepth = (state ushr STATE_EXPRESSION_DEPTH_SHIFT) and 0xFFF
        stringInterpolationDepth = state ushr STATE_STRING_INTERPOLATION_DEPTH_SHIFT
    }

    private fun resolveInitialState(initialState: Int, startOffset: Int): Int {
        if (startOffset == 0) {
            return initialState or STATE_AT_LINE_START
        }
        if (initialState != 0) {
            return initialState
        }

        var offset = startOffset - 1
        while (offset >= 0) {
            val c = buffer[offset]
            when (c) {
                '\n', '\r' -> return initialState or STATE_AT_LINE_START
                ' ', '\t' -> offset--
                else -> return initialState
            }
        }
        return initialState or STATE_AT_LINE_START
    }

    private fun isInsideExpression(): Boolean = expressionDepth > 0
    private fun isInsideStringInterpolation(): Boolean = stringInterpolationDepth > 0

    override fun getBufferSequence(): CharSequence {
        return buffer
    }

    override fun getBufferEnd(): Int {
        return endOffset
    }
}
