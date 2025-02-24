package vos.surface.lexer

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.lexer.LexerPositionImpl
import com.intellij.psi.TokenType.BAD_CHARACTER
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.tree.IElementType
import vos.surface.psi.VosTypes

class VosLexer : Lexer() {
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
        LexerPositionImpl(tokenStart, state)

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
            return VosTypes.COMMENT_DOCUMENT
        }
        if (ch == '/' && peek(1) == '/') {
            skipLineComment()
            return VosTypes.COMMENT
        }
        if (ch == '/' && peek(1) == '*') {
            skipBlockComment()
            return VosTypes.COMMENT_BLOCK
        }

        if (ch == '\'' || ch == '"') {
            return readString(ch)
        }

        if (ch == '.' && peek(1) == '.' && peek(2) == '<') {
            currentOffset += 3
            return VosTypes.RANGE_LE
        }
        if (ch == '.' && peek(1) == '.' && peek(2) == '=') {
            currentOffset += 3
            return VosTypes.RANGE_EQ
        }

        if (ch == '<' && (peek(1) == '=' || peek(1) == '≤' || peek(1) == '⩽')) {
            currentOffset += 2
            return VosTypes.LEQ
        }
        if (ch == '>' && (peek(1) == '=' || peek(1) == '≥' || peek(1) == '⩾')) {
            currentOffset += 2
            return VosTypes.GEQ
        }
        if (ch == '≤' || ch == '⩽') {
            currentOffset++
            return VosTypes.LEQ
        }
        if (ch == '≥' || ch == '⩾') {
            currentOffset++
            return VosTypes.GEQ
        }

        // Keep SIGN as its own token when glued to a number (matches old flex).
        if ((ch == '+' || ch == '-') && isNumberStart(peek(1))) {
            currentOffset++
            return VosTypes.SIGN
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
                    return VosTypes.STRING
                }
                else -> currentOffset++
            }
        }
        return VosTypes.STRING
    }

    private fun readSymbolOrKeyword(): IElementType {
        val start = currentOffset
        while (currentOffset < endOffset && isSymbolPart(buffer[currentOffset])) {
            currentOffset++
        }
        return when (buffer.substring(start, currentOffset)) {
            "let", "var", "const", "object" -> VosTypes.KW_LET
            "union", "enum", "enumerate", "tagged" -> VosTypes.KW_UNION
            "class", "table", "primitive", "struct", "structure" -> VosTypes.KW_CLASS
            "define", "def", "function", "fun", "fn" -> VosTypes.KW_DEFINE
            "namespace", "package" -> VosTypes.KW_NAMESPACE
            "null" -> VosTypes.NULL
            "true", "false" -> VosTypes.BOOLEAN
            else -> VosTypes.SYMBOL
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
                return VosTypes.BYTE
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
                    return VosTypes.DECIMAL
                }
                ch == 'e' || ch == 'E' -> {
                    currentOffset++
                    if (currentOffset < endOffset && (buffer[currentOffset] == '+' || buffer[currentOffset] == '-')) {
                        currentOffset++
                    }
                    while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
                        currentOffset++
                    }
                    return VosTypes.DECIMAL
                }
                ch.isDigit() -> currentOffset++
                else -> break
            }
        }

        return if (sawDot) VosTypes.DECIMAL else VosTypes.INTEGER
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
        return VosTypes.URL
    }

    private fun readSingleCharToken(ch: Char): IElementType {
        currentOffset++
        return when (ch) {
            '(' -> VosTypes.PARENTHESIS_L
            ')' -> VosTypes.PARENTHESIS_R
            '[' -> VosTypes.BRACKET_L
            ']' -> VosTypes.BRACKET_R
            '{' -> VosTypes.BRACE_L
            '}' -> VosTypes.BRACE_R
            '^' -> VosTypes.ACCENT
            '<' -> VosTypes.ANGLE_L
            '>' -> VosTypes.ANGLE_R
            '=' -> VosTypes.EQ
            ':' -> VosTypes.COLON
            ';' -> VosTypes.SEMICOLON
            ',' -> VosTypes.COMMA
            '$' -> VosTypes.DOLLAR
            '.' -> VosTypes.DOT
            '*' -> VosTypes.STAR
            '+', '-' -> VosTypes.SIGN
            '@', '#' -> VosTypes.ANNOTATION_MARK
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
