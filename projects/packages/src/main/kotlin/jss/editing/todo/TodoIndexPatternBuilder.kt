package jss.editing.todo

import com.intellij.lexer.Lexer
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.search.IndexPatternBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import jss.surface.file.JssParserDefinition
import jss.surface.file.JssFileNode
import jss.surface.lexer.JssLexer

class TodoIndexPatternBuilder : IndexPatternBuilder {
    override fun getIndexingLexer(file: PsiFile): Lexer? =
        if (file is JssFileNode) JssLexer() else null

    override fun getCommentTokenSet(file: PsiFile): TokenSet? =
        if (file is JssFileNode) JssParserDefinition.commentTokens else null

    override fun getCommentStartDelta(tokenType: IElementType?): Int =
        if (tokenType in JssParserDefinition.commentTokens) 2 else 0

    override fun getCommentEndDelta(tokenType: IElementType?): Int = 0
}
