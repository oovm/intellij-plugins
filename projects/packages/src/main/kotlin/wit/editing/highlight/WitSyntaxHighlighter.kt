package wit.editing.highlight

import wit.surface.psi.WitLexer
import wit.surface.psi.WitTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase.pack
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class WitSyntaxHighlighter : SyntaxHighlighter {
    override fun getHighlightingLexer(): Lexer = WitLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(colorFor(tokenType)?.textAttributesKey)

    private fun colorFor(tokenType: IElementType): WitHighlightColor? =
        when (tokenType) {
            WitTypes.KW_PACKAGE, WitTypes.KW_INTERFACE, WitTypes.KW_WORLD,
            WitTypes.KW_USE, WitTypes.KW_IMPORT, WitTypes.KW_EXPORT, WitTypes.KW_INCLUDE,
            WitTypes.KW_RECORD, WitTypes.KW_VARIANT, WitTypes.KW_ENUM, WitTypes.KW_FLAGS,
            WitTypes.KW_RESOURCE, WitTypes.KW_TYPE, WitTypes.KW_FUNC, WitTypes.KW_STATIC,
            WitTypes.KW_AS, WitTypes.KW_FROM, WitTypes.KW_WITH, WitTypes.KW_EXTEND, WitTypes.KW_ASYNC,
            WitTypes.KW_BORROW, WitTypes.KW_OWN, WitTypes.KW_CONSTRUCTOR,
            -> WitHighlightColor.KEYWORD

            WitTypes.KW_BOOL, WitTypes.KW_CHAR, WitTypes.KW_STRING,
            WitTypes.KW_U8, WitTypes.KW_U16, WitTypes.KW_U32, WitTypes.KW_U64,
            WitTypes.KW_S8, WitTypes.KW_S16, WitTypes.KW_S32, WitTypes.KW_S64,
            WitTypes.KW_F32, WitTypes.KW_F64,
            WitTypes.KW_LIST, WitTypes.KW_OPTION, WitTypes.KW_RESULT, WitTypes.KW_TUPLE,
            WitTypes.KW_MAP, WitTypes.KW_FUTURE, WitTypes.KW_STREAM,
            -> WitHighlightColor.TYPE

            WitTypes.IDENTIFIER -> WitHighlightColor.IDENTIFIER
            WitTypes.STRING_LITERAL -> WitHighlightColor.STRING
            WitTypes.INTEGER -> WitHighlightColor.NUMBER
            WitTypes.PAREN_L, WitTypes.PAREN_R -> WitHighlightColor.PARENTHESES
            WitTypes.BRACE_L, WitTypes.BRACE_R -> WitHighlightColor.BRACES
            WitTypes.ANGLE_L, WitTypes.ANGLE_R -> WitHighlightColor.BRACKETS
            WitTypes.EQ, WitTypes.COLON, WitTypes.SEMICOLON, WitTypes.COMMA,
            WitTypes.DOT, WitTypes.AT, WitTypes.SLASH, WitTypes.STAR, WitTypes.ARROW,
            -> WitHighlightColor.OPERATOR
            WitTypes.LINE_COMMENT -> WitHighlightColor.LINE_COMMENT
            WitTypes.BLOCK_COMMENT -> WitHighlightColor.BLOCK_COMMENT
            TokenType.BAD_CHARACTER -> WitHighlightColor.BAD_CHARACTER
            else -> null
        }
}
