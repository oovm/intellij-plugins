package com.github.projectfluent.language.psi

import com.github.projectfluent.FluentLanguage
import com.intellij.psi.tree.IElementType

class FluentTokenType(debugName: String) : IElementType(debugName, FluentLanguage) {
    override fun toString(): String = "FluentToken.${super.toString()}"
}

