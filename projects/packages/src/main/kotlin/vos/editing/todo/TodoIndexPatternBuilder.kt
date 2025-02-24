package vos.editing.todo

import com.intellij.lexer.Lexer
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.search.IndexPatternBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import vos.surface.file.VosParserDefinition
import vos.surface.file.VosFileNode
import vos.surface.lexer.VosLexer

class TodoIndexPatternBuilder : IndexPatternBuilder {
    override fun getIndexingLexer(file: PsiFile): Lexer? =
        if (file is VosFileNode) VosLexer() else null

    override fun getCommentTokenSet(file: PsiFile): TokenSet? =
        if (file is VosFileNode) VosParserDefinition.commentTokens else null

    override fun getCommentStartDelta(tokenType: IElementType?): Int =
        if (tokenType in VosParserDefinition.commentTokens) 2 else 0

    override fun getCommentEndDelta(tokenType: IElementType?): Int = 0
}
