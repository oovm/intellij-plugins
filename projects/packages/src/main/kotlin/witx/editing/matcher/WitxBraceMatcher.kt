package witx.editing.matcher

import witx.surface.file.WitxFileType
import witx.surface.psi.WitxParserDefinition
import witx.surface.psi.WitxTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class WitxBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean =
        contextType in ALLOWED_BEFORE

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(WitxTypes.BRACE_L, WitxTypes.BRACE_R, true),
            BracePair(WitxTypes.PAREN_L, WitxTypes.PAREN_R, false),
            BracePair(WitxTypes.ANGLE_L, WitxTypes.ANGLE_R, false),
        )

        private val ALLOWED_BEFORE = TokenSet.orSet(
            WitxParserDefinition.getCommentTokens(),
            TokenSet.create(
                WitxTypes.SEMICOLON,
                WitxTypes.COMMA,
                WitxTypes.COLON,
                TokenType.WHITE_SPACE,
            ),
        )
    }
}
