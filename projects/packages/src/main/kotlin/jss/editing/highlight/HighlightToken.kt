package jss.editing.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import jss.surface.lexer.JssLexer
import jss.surface.psi.JssTypes

class HighlightToken : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = JssLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(getTokenColor(tokenType)?.textAttributesKey)
    }

    private fun getTokenColor(tokenType: IElementType): JssColor? {
        return when (tokenType) {
            JssTypes.KW_NAMESPACE,
            JssTypes.KW_LET,
            JssTypes.KW_DEFINE,
            JssTypes.KW_CLASS,
            JssTypes.KW_UNION,
            -> JssColor.KEYWORD

            JssTypes.ANGLE_L,
            JssTypes.ANGLE_R,
            JssTypes.LEQ,
            JssTypes.GEQ,
            JssTypes.EQ,
            -> JssColor.OPERATOR

            JssTypes.ANNOTATION_MARK -> JssColor.SYM_ANNO
            JssTypes.PARENTHESIS_L, JssTypes.PARENTHESIS_R -> JssColor.PARENTHESES
            JssTypes.BRACKET_L, JssTypes.BRACKET_R -> JssColor.BRACKETS
            JssTypes.BRACE_L, JssTypes.BRACE_R -> JssColor.BRACES
            JssTypes.COLON -> JssColor.SET
            JssTypes.COMMA -> JssColor.COMMA

            JssTypes.INTEGER -> JssColor.INTEGER
            JssTypes.DECIMAL -> JssColor.DECIMAL
            JssTypes.URL -> JssColor.URL
            JssTypes.STRING -> JssColor.STRING
            JssTypes.SYMBOL -> JssColor.IDENTIFIER

            JssTypes.COMMENT -> JssColor.LINE_COMMENT
            JssTypes.COMMENT_BLOCK -> JssColor.BLOCK_COMMENT
            JssTypes.COMMENT_DOCUMENT -> JssColor.DOC_COMMENT

            TokenType.BAD_CHARACTER -> JssColor.BAD_CHARACTER
            else -> null
        }
    }
}
