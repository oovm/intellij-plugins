package witx.surface.psi

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class WitxLexer : Lexer() {
    private var buffer: CharSequence = ""
    private var endOffset: Int = 0
    private var currentOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentToken: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.endOffset = endOffset
        currentOffset = startOffset
        tokenStart = startOffset
        tokenEnd = startOffset
        currentToken = null
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentToken

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        tokenStart = currentOffset
        if (currentOffset >= endOffset) {
            currentToken = null
            tokenEnd = currentOffset
            return
        }

        val c = buffer[currentOffset]
        when {
            c == '/' && peek(1) == '/' -> consumeLineComment()
            c == '/' && peek(1) == '*' -> consumeBlockComment()
            c == '"' -> consumeString()
            c == '-' && peek(1) == '>' -> {
                currentOffset += 2
                currentToken = WitxTypes.ARROW
            }
            c == '=' -> {
                currentOffset++
                currentToken = WitxTypes.EQ
            }
            c == ':' -> {
                currentOffset++
                currentToken = WitxTypes.COLON
            }
            c == ';' -> {
                currentOffset++
                currentToken = WitxTypes.SEMICOLON
            }
            c == ',' -> {
                currentOffset++
                currentToken = WitxTypes.COMMA
            }
            c == '.' -> {
                currentOffset++
                currentToken = WitxTypes.DOT
            }
            c == '@' -> {
                currentOffset++
                currentToken = WitxTypes.AT
            }
            c == '/' -> {
                currentOffset++
                currentToken = WitxTypes.SLASH
            }
            c == '*' -> {
                currentOffset++
                currentToken = WitxTypes.STAR
            }
            c == '(' -> {
                currentOffset++
                currentToken = WitxTypes.PAREN_L
            }
            c == ')' -> {
                currentOffset++
                currentToken = WitxTypes.PAREN_R
            }
            c == '{' -> {
                currentOffset++
                currentToken = WitxTypes.BRACE_L
            }
            c == '}' -> {
                currentOffset++
                currentToken = WitxTypes.BRACE_R
            }
            c == '<' -> {
                currentOffset++
                currentToken = WitxTypes.ANGLE_L
            }
            c == '>' -> {
                currentOffset++
                currentToken = WitxTypes.ANGLE_R
            }
            c.isWhitespace() -> consumeWhitespace()
            c.isDigit() -> consumeInteger()
            c == '%' || c.isLetter() || c == '_' -> consumeIdentifier()
            else -> {
                currentOffset++
                currentToken = TokenType.BAD_CHARACTER
            }
        }
        tokenEnd = currentOffset
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset

    override fun getCurrentPosition(): LexerPosition =
        object : LexerPosition {
            override fun getOffset(): Int = currentOffset
            override fun getState(): Int = getState()
        }

    override fun restore(position: LexerPosition) {
        currentOffset = position.offset
        tokenStart = currentOffset
        tokenEnd = currentOffset
        currentToken = null
        advance()
    }

    private fun peek(offset: Int): Char? {
        val index = currentOffset + offset
        return if (index < endOffset) buffer[index] else null
    }

    private fun consumeWhitespace() {
        while (currentOffset < endOffset && buffer[currentOffset].isWhitespace()) {
            currentOffset++
        }
        currentToken = TokenType.WHITE_SPACE
    }

    private fun consumeLineComment() {
        currentOffset += 2
        while (currentOffset < endOffset && buffer[currentOffset] != '\n') {
            currentOffset++
        }
        currentToken = WitxTypes.LINE_COMMENT
    }

    private fun consumeBlockComment() {
        currentOffset += 2
        var depth = 1
        while (currentOffset < endOffset && depth > 0) {
            if (buffer[currentOffset] == '/' && peek(1) == '*') {
                currentOffset += 2
                depth++
            } else if (buffer[currentOffset] == '*' && peek(1) == '/') {
                currentOffset += 2
                depth--
            } else {
                currentOffset++
            }
        }
        currentToken = WitxTypes.BLOCK_COMMENT
    }

    private fun consumeInteger() {
        while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
            currentOffset++
        }
        currentToken = WitxTypes.INTEGER
    }

    private fun consumeIdentifier() {
        if (buffer[currentOffset] == '%') {
            currentOffset++
        }
        while (currentOffset < endOffset) {
            val ch = buffer[currentOffset]
            if (ch.isLetterOrDigit() || ch == '-' || ch == '_' || ch == '.' || ch == ':' || ch == '/' || ch == '@') {
                currentOffset++
            } else {
                break
            }
        }
        val text = buffer.substring(tokenStart, currentOffset).removePrefix("%")
        currentToken = WitxKeywords.lookup(text) ?: WitxTypes.IDENTIFIER
    }

    private fun consumeString() {
        currentOffset++
        while (currentOffset < endOffset) {
            val ch = buffer[currentOffset]
            when {
                ch == '"' -> {
                    currentOffset++
                    break
                }
                ch == '\\' -> {
                    currentOffset++
                    if (currentOffset < endOffset) {
                        if (buffer[currentOffset] == 'u' && peek(1) == '{') {
                            currentOffset += 2
                            while (currentOffset < endOffset && buffer[currentOffset] != '}') {
                                currentOffset++
                            }
                            if (currentOffset < endOffset) {
                                currentOffset++
                            }
                        } else {
                            currentOffset++
                        }
                    }
                }
                else -> currentOffset++
            }
        }
        currentToken = WitxTypes.STRING_LITERAL
    }
}
