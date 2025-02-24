package vos.surface.psi

import com.intellij.psi.tree.IElementType
import vos.surface.file.VosLanguage

class VosTokenType(debugName: String) : IElementType(debugName, VosLanguage) {
    override fun toString(): String = "VosTokenType.${super.toString()}"
}
