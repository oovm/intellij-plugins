package yggdrasil.surface.psi

import com.intellij.psi.tree.IElementType
import yggdrasil.definition.YggdrasilLanguage


class YggdrasilElementType(debugName: String) : IElementType(debugName, YggdrasilLanguage) {
    override fun toString(): String = "YggdrasilElement.${super.toString()}"
}

