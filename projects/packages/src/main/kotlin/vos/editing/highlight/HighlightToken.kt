package vos.editing.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import vos.surface.lexer.VosLexer
import vos.surface.psi.VosTypes

class HighlightToken : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = VosLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(getTokenColor(tokenType)?.textAttributesKey)
    }

    private fun getTokenColor(tokenType: IElementType): VosColor? {
        return when (tokenType) {
            VosTypes.KW_NAMESPACE,
            VosTypes.KW_LET,
            VosTypes.KW_DEFINE,
            VosTypes.KW_CLASS,
            VosTypes.KW_UNION,
            -> VosColor.KEYWORD

            VosTypes.ANGLE_L,
            VosTypes.ANGLE_R,
            VosTypes.LEQ,
            VosTypes.GEQ,
            VosTypes.EQ,
            -> VosColor.OPERATOR

            VosTypes.ANNOTATION_MARK -> VosColor.SYM_ANNO
            VosTypes.PARENTHESIS_L, VosTypes.PARENTHESIS_R -> VosColor.PARENTHESES
            VosTypes.BRACKET_L, VosTypes.BRACKET_R -> VosColor.BRACKETS
            VosTypes.BRACE_L, VosTypes.BRACE_R -> VosColor.BRACES
            VosTypes.COLON -> VosColor.SET
            VosTypes.COMMA -> VosColor.COMMA

            VosTypes.INTEGER -> VosColor.INTEGER
            VosTypes.DECIMAL -> VosColor.DECIMAL
            VosTypes.URL -> VosColor.URL
            VosTypes.STRING -> VosColor.STRING
            VosTypes.SYMBOL -> VosColor.IDENTIFIER

            VosTypes.COMMENT -> VosColor.LINE_COMMENT
            VosTypes.COMMENT_BLOCK -> VosColor.BLOCK_COMMENT
            VosTypes.COMMENT_DOCUMENT -> VosColor.DOC_COMMENT

            TokenType.BAD_CHARACTER -> VosColor.BAD_CHARACTER
            else -> null
        }
    }
}
