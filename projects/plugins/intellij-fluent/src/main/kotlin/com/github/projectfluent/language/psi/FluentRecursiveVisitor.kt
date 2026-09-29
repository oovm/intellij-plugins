package com.github.projectfluent.language.psi


import com.github.projectfluent.language.psi.nodes.FluentAttributeIDNode
import com.github.projectfluent.language.psi.nodes.FluentAttributeNode
import com.github.projectfluent.language.psi.nodes.FluentFunctionIDNode
import com.github.projectfluent.language.psi.nodes.FluentInlinePlaceableNode
import com.github.projectfluent.language.psi.nodes.FluentMessageIDNode
import com.github.projectfluent.language.psi.nodes.FluentMessageNode
import com.github.projectfluent.language.psi.nodes.FluentTermIDNode
import com.github.projectfluent.language.psi.nodes.FluentTermNode
import com.github.projectfluent.language.psi.nodes.FluentVariableIDNode
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
