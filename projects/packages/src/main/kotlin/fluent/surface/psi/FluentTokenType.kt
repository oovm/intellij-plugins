package fluent.surface.psi

import fluent.definition.FluentLanguage
import com.intellij.psi.tree.IElementType

class FluentTokenType(debugName: String) : IElementType(debugName, FluentLanguage) {
    override fun toString(): String = "FluentToken.${super.toString()}"
}

