package wit.surface.psi

import wit.definition.WitLanguage
import com.intellij.psi.tree.IElementType

class WitTokenType(name: String) : IElementType(name, WitLanguage) {
    private val tokenName: String = name

    override fun toString(): String = "WitToken.$tokenName"
}
