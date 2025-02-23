package com.github.bytecodealliance.wit.language.psi

import com.github.bytecodealliance.wit.WitLanguage
import com.intellij.psi.tree.IElementType

class WitTokenType(debugName: String) : IElementType(debugName, WitLanguage) {
    override fun toString(): String = "WitToken.$debugName"
}
