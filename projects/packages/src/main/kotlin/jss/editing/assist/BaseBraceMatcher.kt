package jss.editing.assist

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import jss.surface.file.JssParserDefinition
import jss.surface.psi.JssTypes

class BaseBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean {
        return next in InsertPairBraceBefore
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(JssTypes.BRACE_L, JssTypes.BRACE_R, true),
            BracePair(JssTypes.BRACKET_L, JssTypes.BRACKET_R, true),
            BracePair(JssTypes.PARENTHESIS_L, JssTypes.PARENTHESIS_R, true),
        )

        private val InsertPairBraceBefore = TokenSet.orSet(
            JssParserDefinition.commentTokens,
            TokenSet.create(
                TokenType.WHITE_SPACE,
                JssTypes.COMMA,
                JssTypes.PARENTHESIS_R,
                JssTypes.BRACKET_R,
                JssTypes.BRACE_R,
            ),
        )
    }
}
