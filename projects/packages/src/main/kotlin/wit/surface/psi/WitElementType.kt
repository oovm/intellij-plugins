package wit.surface.psi

import wit.definition.WitLanguage
import com.intellij.psi.tree.IElementType

class WitElementType(name: String) : IElementType(name, WitLanguage) {
    private val elementName: String = name

    override fun toString(): String = "WitElement.$elementName"
}
