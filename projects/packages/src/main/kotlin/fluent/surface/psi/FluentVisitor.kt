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
import com.intellij.psi.PsiElementVisitor

open class FluentVisitor : PsiElementVisitor() {
    open fun visitMessage(message: FluentMessageNode) {}
    open fun visitTerm(term: FluentTermNode) {}
    open fun visitAttribute(attribute: FluentAttributeNode) {}
    open fun visitMessageID(messageID: FluentMessageIDNode) {}
    open fun visitTermID(termID: FluentTermIDNode) {}
    open fun visitAttributeID(attributeID: FluentAttributeIDNode) {}
    open fun visitVariableID(variableID: FluentVariableIDNode) {}
    open fun visitFunctionID(functionID: FluentFunctionIDNode) {}
    open fun visitInlinePlaceable(inlinePlaceable: FluentInlinePlaceableNode) {}
}
