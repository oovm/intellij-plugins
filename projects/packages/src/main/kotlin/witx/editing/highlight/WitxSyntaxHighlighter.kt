package witx.editing.highlight

import witx.surface.psi.WitxLexer
import witx.surface.psi.WitxTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase.pack
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class WitxSyntaxHighlighter : SyntaxHighlighter {
    override fun getHighlightingLexer(): Lexer = WitxLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(colorFor(tokenType)?.textAttributesKey)

    private fun colorFor(tokenType: IElementType): WitxHighlightColor? =
        when (tokenType) {
            WitxTypes.KW_PACKAGE, WitxTypes.KW_INTERFACE, WitxTypes.KW_WORLD,
            WitxTypes.KW_USE, WitxTypes.KW_IMPORT, WitxTypes.KW_EXPORT, WitxTypes.KW_INCLUDE,
            WitxTypes.KW_RECORD, WitxTypes.KW_VARIANT, WitxTypes.KW_ENUM, WitxTypes.KW_FLAGS,
            WitxTypes.KW_RESOURCE, WitxTypes.KW_TYPE, WitxTypes.KW_FUNC, WitxTypes.KW_STATIC,
            WitxTypes.KW_AS, WitxTypes.KW_FROM, WitxTypes.KW_WITH, WitxTypes.KW_EXTEND, WitxTypes.KW_ASYNC,
            WitxTypes.KW_BORROW, WitxTypes.KW_OWN, WitxTypes.KW_CONSTRUCTOR,
            -> WitxHighlightColor.KEYWORD

            WitxTypes.KW_BOOL, WitxTypes.KW_CHAR, WitxTypes.KW_STRING,
            WitxTypes.KW_U8, WitxTypes.KW_U16, WitxTypes.KW_U32, WitxTypes.KW_U64,
            WitxTypes.KW_S8, WitxTypes.KW_S16, WitxTypes.KW_S32, WitxTypes.KW_S64,
            WitxTypes.KW_F32, WitxTypes.KW_F64,
            WitxTypes.KW_LIST, WitxTypes.KW_OPTION, WitxTypes.KW_RESULT, WitxTypes.KW_TUPLE,
            WitxTypes.KW_MAP, WitxTypes.KW_FUTURE, WitxTypes.KW_STREAM,
            -> WitxHighlightColor.TYPE

            WitxTypes.IDENTIFIER -> WitxHighlightColor.IDENTIFIER
            WitxTypes.STRING_LITERAL -> WitxHighlightColor.STRING
            WitxTypes.INTEGER -> WitxHighlightColor.NUMBER
            WitxTypes.PAREN_L, WitxTypes.PAREN_R -> WitxHighlightColor.PARENTHESES
            WitxTypes.BRACE_L, WitxTypes.BRACE_R -> WitxHighlightColor.BRACES
            WitxTypes.ANGLE_L, WitxTypes.ANGLE_R -> WitxHighlightColor.BRACKETS
            WitxTypes.EQ, WitxTypes.COLON, WitxTypes.SEMICOLON, WitxTypes.COMMA,
            WitxTypes.DOT, WitxTypes.AT, WitxTypes.SLASH, WitxTypes.STAR, WitxTypes.ARROW,
            -> WitxHighlightColor.OPERATOR
            WitxTypes.LINE_COMMENT -> WitxHighlightColor.LINE_COMMENT
            WitxTypes.BLOCK_COMMENT -> WitxHighlightColor.BLOCK_COMMENT
            TokenType.BAD_CHARACTER -> WitxHighlightColor.BAD_CHARACTER
            else -> null
        }
}
