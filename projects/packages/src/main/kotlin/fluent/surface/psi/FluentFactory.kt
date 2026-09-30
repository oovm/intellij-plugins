package fluent.surface.psi

import fluent.surface.psi.nodes.FluentAttributeIDNode
import fluent.surface.psi.nodes.FluentAttributeNode
import fluent.surface.psi.nodes.FluentFunctionIDNode
import fluent.surface.psi.nodes.FluentInlinePlaceableNode
import fluent.surface.psi.nodes.FluentMessageIDNode
import fluent.surface.psi.nodes.FluentMessageNode
import fluent.surface.psi.nodes.FluentTermIDNode
import fluent.surface.psi.nodes.FluentTermNode
import fluent.surface.psi.nodes.FluentVariableIDNode
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

// Factory
object FluentFactory {
    fun createElement(node: ASTNode): PsiElement {
        return when (node.elementType) {
            FluentTypes.MESSAGE -> FluentMessageNode(node)
            FluentTypes.TERM -> FluentTermNode(node)
            FluentTypes.ATTRIBUTE -> FluentAttributeNode(node)
            FluentTypes.MESSAGE_ID -> FluentMessageIDNode(node)
            FluentTypes.TERM_ID -> FluentTermIDNode(node)
            FluentTypes.ATTRIBUTE_ID -> FluentAttributeIDNode(node)
            FluentTypes.VARIABLE_ID -> FluentVariableIDNode(node)
            FluentTypes.FUNCTION_ID -> FluentFunctionIDNode(node)
            FluentTypes.INLINE_PLACEABLE -> FluentInlinePlaceableNode(node)
            else -> FluentElement(node)
        }
    }
}