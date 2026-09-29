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