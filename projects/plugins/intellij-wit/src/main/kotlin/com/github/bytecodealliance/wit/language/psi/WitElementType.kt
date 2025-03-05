package com.github.bytecodealliance.wit.language.psi

import com.github.bytecodealliance.wit.WitLanguage
import com.intellij.psi.tree.IElementType

class WitElementType(name: String) : IElementType(name, WitLanguage) {
    private val elementName: String = name

    override fun toString(): String = "WitElement.$elementName"
}
