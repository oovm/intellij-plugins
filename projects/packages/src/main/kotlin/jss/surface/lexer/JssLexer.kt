package jss.surface.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.TokenType.BAD_CHARACTER
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IElementType
import jss.surface.psi.JssTypes

class JssLexer : Lexer() {
    private var buffer: CharSequence = ""
    private var tokenStart = 0
    private var currentOffset = 0
    private var endOffset = 0
    private var currentTokenType: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.tokenStart = startOffset
        this.currentOffset = startOffset
        this.endOffset = endOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentTokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = currentOffset

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        tokenStart = currentOffset
        if (currentOffset >= endOffset) {
            currentTokenType = null
            return
        }
        currentTokenType = nextToken()
    }

    override fun getCurrentPosition(): LexerPosition =
        object : LexerPosition {
            override fun getOffset(): Int = currentOffset
            override fun getState(): Int = state
        }

    override fun restore(position: LexerPosition) {
        start(buffer, position.offset, endOffset, position.state)
    }

    private fun nextToken(): IElementType {
        val ch = buffer[currentOffset]

        if (ch.isWhitespace()) {
            while (currentOffset < endOffset && buffer[currentOffset].isWhitespace()) {
                currentOffset++
            }
            return WHITE_SPACE
        }

        if (ch == '/' && peek(1) == '/' && peek(2) == '/') {
            skipLineComment()
            return JssTypes.COMMENT_DOCUMENT
        }
        if (ch == '/' && peek(1) == '/') {
            skipLineComment()
            return JssTypes.COMMENT
        }
        if (ch == '/' && peek(1) == '*') {
            skipBlockComment()
            return JssTypes.COMMENT_BLOCK
        }

        if (ch == '\'' || ch == '"') {
            return readString(ch)
        }

        if (ch == '.' && peek(1) == '.' && peek(2) == '<') {
            currentOffset += 3
            return JssTypes.RANGE_LE
        }
        if (ch == '.' && peek(1) == '.' && peek(2) == '=') {
            currentOffset += 3
            return JssTypes.RANGE_EQ
        }

        if (ch == '<' && (peek(1) == '=' || peek(1) == '≤' || peek(1) == '⩽')) {
            currentOffset += 2
            return JssTypes.LEQ
        }
        if (ch == '>' && (peek(1) == '=' || peek(1) == '≥' || peek(1) == '⩾')) {
            currentOffset += 2
            return JssTypes.GEQ
        }
        if (ch == '≤' || ch == '⩽') {
            currentOffset++
            return JssTypes.LEQ
        }
        if (ch == '≥' || ch == '⩾') {
            currentOffset++
            return JssTypes.GEQ
        }

        // Keep SIGN as its own token when glued to a number (matches old flex).
        if ((ch == '+' || ch == '-') && isNumberStart(peek(1))) {
            currentOffset++
            return JssTypes.SIGN
        }

        if (ch.isDigit()) {
            return readNumber()
        }

        if (isUrlStart()) {
            return readUrl()
        }

        if (isSymbolStart(ch)) {
            return readSymbolOrKeyword()
        }

        return readSingleCharToken(ch)
    }

    private fun readString(quote: Char): IElementType {
        currentOffset++
        while (currentOffset < endOffset) {
            when (buffer[currentOffset]) {
                '\\' -> {
                    currentOffset++
                    if (currentOffset < endOffset) currentOffset++
                }
                quote -> {
                    currentOffset++
                    return JssTypes.STRING
                }
                else -> currentOffset++
            }
        }
        return JssTypes.STRING
    }

    private fun readSymbolOrKeyword(): IElementType {
        val start = currentOffset
        while (currentOffset < endOffset && isSymbolPart(buffer[currentOffset])) {
            currentOffset++
        }
        return when (buffer.substring(start, currentOffset)) {
            "let", "var", "const", "object" -> JssTypes.KW_LET
            "union", "enum", "enumerate", "tagged" -> JssTypes.KW_UNION
            "class", "table", "primitive", "struct", "structure" -> JssTypes.KW_CLASS
            "define", "def", "function", "fun", "fn" -> JssTypes.KW_DEFINE
            "namespace", "package" -> JssTypes.KW_NAMESPACE
            "null" -> JssTypes.NULL
            "true", "false" -> JssTypes.BOOLEAN
            else -> JssTypes.SYMBOL
        }
    }

    private fun readNumber(): IElementType {
        if (currentOffset < endOffset && buffer[currentOffset] == '0') {
            val next = peek(1)
            if (next == 'b' || next == 'B' || next == 'o' || next == 'O' ||
                next == 'x' || next == 'X' || next == 'f' || next == 'F'
            ) {
                currentOffset += 2
                while (currentOffset < endOffset && isBytePart(buffer[currentOffset])) {
                    currentOffset++
                }
                return JssTypes.BYTE
            }
        }

        var sawDot = false
        while (currentOffset < endOffset) {
            val ch = buffer[currentOffset]
            when {
                ch == '_' -> currentOffset++
                ch == '.' -> {
                    if (sawDot || !peek(1).isDigit()) break
                    sawDot = true
                    currentOffset++
                }
                ch == '*' && peek(1) == '*' -> {
                    currentOffset += 2
                    if (currentOffset < endOffset && (buffer[currentOffset] == '+' || buffer[currentOffset] == '-')) {
                        currentOffset++
                    }
                    while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
                        currentOffset++
                    }
                    return JssTypes.DECIMAL
                }
                ch == 'e' || ch == 'E' -> {
                    currentOffset++
                    if (currentOffset < endOffset && (buffer[currentOffset] == '+' || buffer[currentOffset] == '-')) {
                        currentOffset++
                    }
                    while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
                        currentOffset++
                    }
                    return JssTypes.DECIMAL
                }
                ch.isDigit() -> currentOffset++
                else -> break
            }
        }

        return if (sawDot) JssTypes.DECIMAL else JssTypes.INTEGER
    }

    private fun isUrlStart(): Boolean {
        if (!buffer[currentOffset].isLetterOrDigit()) return false
        var i = currentOffset
        while (i < endOffset && buffer[i].isLetterOrDigit()) {
            i++
        }
        if (i >= endOffset || buffer[i] != ':') return false
        if (i + 2 >= endOffset || buffer[i + 1] != '/' || buffer[i + 2] != '/') return false
        return true
    }

    private fun readUrl(): IElementType {
        while (currentOffset < endOffset && buffer[currentOffset].isLetterOrDigit()) {
            currentOffset++
        }
        currentOffset += 3
        while (currentOffset < endOffset) {
            val ch = buffer[currentOffset]
            if (ch == '-' || ch == '.' || ch == '/' || ch == '?' || ch == '&' || ch == '#' ||
                ch.isLetterOrDigit() || Character.isUnicodeIdentifierPart(ch)
            ) {
                currentOffset++
            } else {
                break
            }
        }
        return JssTypes.URL
    }

    private fun readSingleCharToken(ch: Char): IElementType {
        currentOffset++
        return when (ch) {
            '(' -> JssTypes.PARENTHESIS_L
            ')' -> JssTypes.PARENTHESIS_R
            '[' -> JssTypes.BRACKET_L
            ']' -> JssTypes.BRACKET_R
            '{' -> JssTypes.BRACE_L
            '}' -> JssTypes.BRACE_R
            '^' -> JssTypes.ACCENT
            '<' -> JssTypes.ANGLE_L
            '>' -> JssTypes.ANGLE_R
            '=' -> JssTypes.EQ
            ':' -> JssTypes.COLON
            ';' -> JssTypes.SEMICOLON
            ',' -> JssTypes.COMMA
            '$' -> JssTypes.DOLLAR
            '.' -> JssTypes.DOT
            '*' -> JssTypes.STAR
            '+', '-' -> JssTypes.SIGN
            '@', '#' -> JssTypes.ANNOTATION_MARK
            else -> BAD_CHARACTER
        }
    }

    private fun skipLineComment() {
        while (currentOffset < endOffset) {
            val ch = buffer[currentOffset]
            currentOffset++
            if (ch == '\n' || ch == '\r') break
        }
    }

    private fun skipBlockComment() {
        currentOffset += 2
        while (currentOffset < endOffset) {
            if (buffer[currentOffset] == '*' && peek(1) == '/') {
                currentOffset += 2
                return
            }
            currentOffset++
        }
    }

    private fun isNumberStart(ch: Char): Boolean = ch.isDigit()

    private fun isSymbolStart(ch: Char): Boolean =
        Character.isUnicodeIdentifierStart(ch) || ch == '_' || ch == '$'

    private fun isSymbolPart(ch: Char): Boolean =
        Character.isUnicodeIdentifierPart(ch) || ch == '_' || ch == '$'

    private fun isBytePart(ch: Char): Boolean =
        ch.isDigit() || ch in 'A'..'F' || ch in 'a'..'f' || ch == '_'

    private fun peek(offset: Int): Char {
        val index = currentOffset + offset
        return if (index < endOffset) buffer[index] else '\u0000'
    }
}
