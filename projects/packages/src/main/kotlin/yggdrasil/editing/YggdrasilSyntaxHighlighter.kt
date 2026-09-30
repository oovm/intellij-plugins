package yggdrasil.editing.highlight


import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase.pack
import com.intellij.psi.tree.IElementType
import yggdrasil.surface.psi.YggdrasilTypes
import yggdrasil.surface.psi.YggdrasilLexer

class YggdrasilSyntaxHighlighter : SyntaxHighlighter {
    override fun getHighlightingLexer(): Lexer {
        return YggdrasilLexer()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(getTokenColor(tokenType)?.textAttributesKey)
    }

    private fun getTokenColor(tokenType: IElementType): HighlightColor? {
        return when (tokenType) {
            YggdrasilTypes.KW_GRAMMAR, YggdrasilTypes.KW_USING,
            YggdrasilTypes.KW_CLASS, YggdrasilTypes.KW_UNION, YggdrasilTypes.KW_GROUP, YggdrasilTypes.KW_MACRO,
                -> HighlightColor.KEYWORD
            // operations
            YggdrasilTypes.COLON, YggdrasilTypes.BIND -> HighlightColor.OPERATION
            YggdrasilTypes.OP_OR, YggdrasilTypes.OP_CONCAT -> HighlightColor.OPERATION
            YggdrasilTypes.OP_REMARK -> HighlightColor.OPERATION
            YggdrasilTypes.OP_OPTIONAL, YggdrasilTypes.OP_MANY, YggdrasilTypes.OP_MANY1 -> HighlightColor.OPERATION
            // literals
            YggdrasilTypes.TEXT_SINGLE, YggdrasilTypes.TEXT_DOUBLE -> HighlightColor.STRING
            YggdrasilTypes.ESCAPED -> HighlightColor.STRING_ESCAPED
            YggdrasilTypes.INTEGER -> HighlightColor.INTEGER
            // comments
            YggdrasilTypes.COMMENT_LINE -> HighlightColor.COMMENT_LINE
            YggdrasilTypes.COMMENT_BLOCK -> HighlightColor.COMMENT_BLOCK
            // errors
//            TokenType.BAD_CHARACTER -> YggdrasilHighlightColor.BAD_CHARACTER

            else -> null
        }
    }
}
