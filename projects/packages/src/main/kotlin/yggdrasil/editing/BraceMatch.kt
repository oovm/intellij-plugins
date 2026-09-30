package yggdrasil.editing.assist

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import yggdrasil.surface.psi.YggdrasilTypes


class BraceMatch : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> {
        return arrayOf(
            BracePair(YggdrasilTypes.BRACE_L, YggdrasilTypes.BRACE_R, true),
            BracePair(YggdrasilTypes.BRACKET_L, YggdrasilTypes.BRACKET_R, true),
            BracePair(YggdrasilTypes.PARENTHESIS_L, YggdrasilTypes.PARENTHESIS_R, true),
        )
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean {
        return false
    }

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

    companion object {
        val Left = TokenSet.create(
            YggdrasilTypes.PARENTHESIS_L, YggdrasilTypes.BRACKET_L, YggdrasilTypes.BRACE_L,
        )
        val Right = TokenSet.create(
            YggdrasilTypes.PARENTHESIS_R, YggdrasilTypes.BRACKET_R, YggdrasilTypes.BRACE_R,
        )
    }
}
