package witx.surface.psi

import witx.definition.WitxLanguage
import com.intellij.psi.tree.IElementType

class WitxTokenType(name: String) : IElementType(name, WitxLanguage) {
    private val tokenName: String = name

    override fun toString(): String = "WitxToken.$tokenName"
}
