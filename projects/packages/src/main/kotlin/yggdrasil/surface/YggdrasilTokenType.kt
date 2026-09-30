package yggdrasil.surface.psi

import com.intellij.psi.tree.IElementType
import yggdrasil.definition.YggdrasilLanguage

class YggdrasilTokenType(debugName: String) : IElementType(debugName, YggdrasilLanguage) {
    override fun toString(): String = "YggdrasilToken.${super.toString()}"
}