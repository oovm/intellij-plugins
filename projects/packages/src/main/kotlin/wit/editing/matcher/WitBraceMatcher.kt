package wit.editing.matcher

import wit.surface.file.WitFileType
import wit.surface.psi.WitParserDefinition
import wit.surface.psi.WitTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class WitBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean =
        contextType in ALLOWED_BEFORE

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(WitTypes.BRACE_L, WitTypes.BRACE_R, true),
            BracePair(WitTypes.PAREN_L, WitTypes.PAREN_R, false),
            BracePair(WitTypes.ANGLE_L, WitTypes.ANGLE_R, false),
        )

        private val ALLOWED_BEFORE = TokenSet.orSet(
            WitParserDefinition.getCommentTokens(),
            TokenSet.create(
                WitTypes.SEMICOLON,
                WitTypes.COMMA,
                WitTypes.COLON,
                TokenType.WHITE_SPACE,
            ),
        )
    }
}
