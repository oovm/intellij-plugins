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
