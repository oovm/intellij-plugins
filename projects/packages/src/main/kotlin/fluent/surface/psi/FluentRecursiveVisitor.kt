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
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveVisitor

open class FluentRecursiveVisitor : FluentVisitor(), PsiRecursiveVisitor {
    override fun visitElement(element: PsiElement) {
        ProgressManager.checkCanceled()
        element.acceptChildren(this)
    }

    override fun visitMessage(message: FluentMessageNode) {
        visitElement(message)
    }

    override fun visitTerm(term: FluentTermNode) {
        visitElement(term)
    }

    override fun visitAttribute(attribute: FluentAttributeNode) {
        visitElement(attribute)
    }

    override fun visitMessageID(messageID: FluentMessageIDNode) {
        visitElement(messageID)
    }

    override fun visitTermID(termID: FluentTermIDNode) {
        visitElement(termID)
    }

    override fun visitAttributeID(attributeID: FluentAttributeIDNode) {
        visitElement(attributeID)
    }

    override fun visitVariableID(variableID: FluentVariableIDNode) {
        visitElement(variableID)
    }

    override fun visitFunctionID(functionID: FluentFunctionIDNode) {
        visitElement(functionID)
    }

    override fun visitInlinePlaceable(inlinePlaceable: FluentInlinePlaceableNode) {
        visitElement(inlinePlaceable)
    }
}
