package com.github.bytecodealliance.wit.language.psi

import com.github.bytecodealliance.wit.WitLanguage
import com.intellij.psi.tree.IElementType

class WitTokenType(name: String) : IElementType(name, WitLanguage) {
    private val tokenName: String = name

    override fun toString(): String = "WitToken.$tokenName"
}
