package fluent.surface.psi

import fluent.definition.FluentLanguage
import com.intellij.psi.tree.IElementType

class FluentElementType(debugName: String) : IElementType(debugName, FluentLanguage) {
    override fun toString(): String = "FluentElement.${super.toString()}"
}
