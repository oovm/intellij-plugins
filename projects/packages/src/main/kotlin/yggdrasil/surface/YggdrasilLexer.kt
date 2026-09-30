package yggdrasil.surface.psi

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.psi.tree.IElementType
import com.intellij.psi.TokenType

class YggdrasilLexer : Lexer() {
    private lateinit var buffer: CharSequence
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentOffset: Int = 0
    private var state: Int = 0
    private var currentTokenType: IElementType? = null
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0

    // Keywords
    private val keywords = setOf(
        "grammar", "lexicon", "using", "group", "token",
        "operators", "class", "struct", "union", "enum",
        "climb", "import", "as", "macro", "function",
        "func", "fun", "fn", "def"
    )

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentOffset = startOffset
        this.state = initialState
        this.currentTokenType = null
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = state

    override fun getTokenType(): IElementType? = currentTokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        if (currentOffset >= endOffset) {
            currentTokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        tokenStart = currentOffset
        val currentChar = buffer[currentOffset]

        when (currentChar) {
            in '\t'..' ' -> parseWhitespace()
            '/' -> parseCommentOrSlash()
            '\'' -> parseStringSingle()
            '"' -> parseStringDouble()
            in '0'..'9' -> parseNumber()
            in 'a'..'z', in 'A'..'Z', '_' -> parseIdentifier()
            '`' -> parseRawSymbol()
            '\\' -> parseEscaped()
            '[' -> parseRegularRange()
            '(' -> parsePunctuation(YggdrasilTypes.PARENTHESIS_L)
            ')' -> parsePunctuation(YggdrasilTypes.PARENTHESIS_R)
            ']' -> parsePunctuation(YggdrasilTypes.BRACKET_R)
            '{' -> parsePunctuation(YggdrasilTypes.BRACE_L)
            '}' -> parsePunctuation(YggdrasilTypes.BRACE_R)
            '<' -> parsePunctuation(YggdrasilTypes.ANGLE_L)
            '>' -> parsePunctuation(YggdrasilTypes.ANGLE_R)
            '|' -> parsePunctuation(YggdrasilTypes.OP_OR)
            '~' -> parsePunctuation(YggdrasilTypes.OP_CONCAT)
            ':' -> parsePunctuation(YggdrasilTypes.COLON)
            ';' -> parsePunctuation(YggdrasilTypes.SEMICOLON)
            '#' -> parsePunctuation(YggdrasilTypes.HASH)
            '$' -> parsePunctuation(YggdrasilTypes.DOLLAR)
            '@' -> parsePunctuation(YggdrasilTypes.AT)
            '.' -> parsePunctuation(YggdrasilTypes.DOT)
            ',' -> parsePunctuation(YggdrasilTypes.COMMA)
            '^' -> parsePunctuation(YggdrasilTypes.OP_REMARK)
            '!' -> parsePunctuation(YggdrasilTypes.OP_NOT)
            '?' -> parsePunctuation(YggdrasilTypes.OP_OPTIONAL)
            '*' -> parsePunctuation(YggdrasilTypes.OP_MANY)
            '+' -> parsePunctuation(YggdrasilTypes.OP_MANY1)
            '-' -> parseHyphenOrArrow()
            '=' -> parsePunctuation(YggdrasilTypes.BIND)
            else -> parseBadCharacter()
        }
    }

    private fun parseWhitespace() {
        currentTokenType = TokenType.WHITE_SPACE
        while (currentOffset < endOffset && buffer[currentOffset] in '\t'..' ') {
            currentOffset++
        }
        tokenEnd = currentOffset
    }

    private fun parseCommentOrSlash() {
        if (currentOffset + 1 < endOffset) {
            val nextChar = buffer[currentOffset + 1]
            when (nextChar) {
                '/' -> parseLineComment()
                '*' -> parseBlockComment()
                else -> parsePunctuation(YggdrasilTypes.SLASH)
            }
        } else {
            parsePunctuation(YggdrasilTypes.SLASH)
        }
    }

    private fun parseLineComment() {
        currentTokenType = YggdrasilTypes.COMMENT_LINE
        currentOffset += 2 // skip "//"
        while (currentOffset < endOffset && buffer[currentOffset] != '\n') {
            currentOffset++
        }
        tokenEnd = currentOffset
    }

    private fun parseBlockComment() {
        currentTokenType = YggdrasilTypes.COMMENT_BLOCK
        currentOffset += 2 // skip "/*"
        while (currentOffset + 1 < endOffset) {
            if (buffer[currentOffset] == '*' && buffer[currentOffset + 1] == '/') {
                currentOffset += 2 // skip "*/"
                break
            }
            currentOffset++
        }
        tokenEnd = currentOffset
    }

    private fun parseStringSingle() {
        currentTokenType = YggdrasilTypes.TEXT_SINGLE
        currentOffset++ // skip opening quote
        while (currentOffset < endOffset && buffer[currentOffset] != '\'') {
            if (buffer[currentOffset] == '\\') {
                currentOffset++ // skip escape character
            }
            currentOffset++
        }
        if (currentOffset < endOffset) {
            currentOffset++ // skip closing quote
        }
        tokenEnd = currentOffset
    }

    private fun parseStringDouble() {
        currentTokenType = YggdrasilTypes.TEXT_DOUBLE
        currentOffset++ // skip opening quote
        while (currentOffset < endOffset && buffer[currentOffset] != '"') {
            if (buffer[currentOffset] == '\\') {
                currentOffset++ // skip escape character
            }
            currentOffset++
        }
        if (currentOffset < endOffset) {
            currentOffset++ // skip closing quote
        }
        tokenEnd = currentOffset
    }

    private fun parseNumber() {
        currentTokenType = YggdrasilTypes.INTEGER
        while (currentOffset < endOffset && buffer[currentOffset].isDigit()) {
            currentOffset++
        }
        tokenEnd = currentOffset
    }

    private fun parseIdentifier() {
        val start = currentOffset
        while (currentOffset < endOffset && (buffer[currentOffset].isLetterOrDigit() || buffer[currentOffset] == '_')) {
            currentOffset++
        }
        val identifier = buffer.substring(start, currentOffset)
        currentTokenType = if (identifier in keywords) {
            when (identifier) {
                "grammar", "lexicon" -> YggdrasilTypes.KW_GRAMMAR
                "using" -> YggdrasilTypes.KW_USING
                "group", "token" -> YggdrasilTypes.KW_GROUP
                "operators" -> YggdrasilTypes.KW_OPERATORS
                "class", "struct" -> YggdrasilTypes.KW_CLASS
                "union", "enum", "climb" -> YggdrasilTypes.KW_UNION
                "import" -> YggdrasilTypes.KW_IMPORT
                "as" -> YggdrasilTypes.KW_AS
                "macro", "function", "func", "fun", "fn", "def" -> YggdrasilTypes.KW_MACRO
                else -> YggdrasilTypes.SYMBOL
            }
        } else {
            YggdrasilTypes.SYMBOL
        }
        tokenEnd = currentOffset
    }

    private fun parseRawSymbol() {
        currentTokenType = YggdrasilTypes.SYMBOW_RAW
        currentOffset++ // skip opening backtick
        while (currentOffset < endOffset && buffer[currentOffset] != '`') {
            currentOffset++
        }
        if (currentOffset < endOffset) {
            currentOffset++ // skip closing backtick
        }
        tokenEnd = currentOffset
    }

    private fun parseEscaped() {
        currentTokenType = YggdrasilTypes.ESCAPED
        currentOffset++ // skip backslash
        if (currentOffset < endOffset) {
            currentOffset++ // skip the escaped character
        }
        tokenEnd = currentOffset
    }

    private fun parseRegularRange() {
        currentTokenType = YggdrasilTypes.REGULAR_RANGE
        currentOffset++ // skip opening bracket
        while (currentOffset < endOffset && buffer[currentOffset] != ']') {
            currentOffset++
        }
        if (currentOffset < endOffset) {
            currentOffset++ // skip closing bracket
        }
        tokenEnd = currentOffset
    }

    private fun parsePunctuation(type: IElementType) {
        currentTokenType = type
        currentOffset++
        tokenEnd = currentOffset
    }

    private fun parseHyphenOrArrow() {
        if (currentOffset + 1 < endOffset && buffer[currentOffset + 1] == '>') {
            currentTokenType = YggdrasilTypes.TO
            currentOffset += 2
        } else {
            currentTokenType = YggdrasilTypes.HYPHEN
            currentOffset++
        }
        tokenEnd = currentOffset
    }

    private fun parseBadCharacter() {
        currentTokenType = TokenType.BAD_CHARACTER
        currentOffset++
        tokenEnd = currentOffset
    }

    override fun getCurrentPosition(): LexerPosition {
        return object : LexerPosition {
            override fun getOffset(): Int = currentOffset
            override fun getState(): Int = state
        }
    }

    override fun restore(position: LexerPosition) {
        currentOffset = position.offset
        state = position.state
        advance()
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset
}
