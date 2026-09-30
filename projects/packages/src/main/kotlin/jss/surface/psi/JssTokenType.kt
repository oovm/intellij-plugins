package jss.surface.psi

import com.intellij.psi.tree.IElementType
import jss.surface.file.JssLanguage

class JssTokenType(debugName: String) : IElementType(debugName, JssLanguage) {
    override fun toString(): String = "JssTokenType.${super.toString()}"
}
