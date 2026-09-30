package witx.surface.psi

import witx.definition.WitxLanguage
import com.intellij.psi.tree.IElementType

class WitxElementType(name: String) : IElementType(name, WitxLanguage) {
    private val elementName: String = name

    override fun toString(): String = "WitxElement.$elementName"
}
