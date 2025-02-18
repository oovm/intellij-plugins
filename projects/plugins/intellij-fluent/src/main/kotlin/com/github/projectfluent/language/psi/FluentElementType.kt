package com.github.projectfluent.language.psi

import com.github.projectfluent.FluentLanguage
import com.intellij.psi.tree.IElementType

class FluentElementType(debugName: String) : IElementType(debugName, FluentLanguage) {
    override fun toString(): String = "FluentElement.${super.toString()}"
}
