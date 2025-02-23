package com.github.bytecodealliance.wit.language.psi.nodes

import com.github.bytecodealliance.wit.language.psi.WitElement
import com.github.bytecodealliance.wit.language.psi.WitTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil

open class WitNamedDeclNode(node: ASTNode) : WitElement(node), PsiNamedElement {
    override fun getName(): String? =
        PsiTreeUtil.findChildrenOfType(this, com.intellij.psi.PsiElement::class.java)
            .firstOrNull { it.node?.elementType == WitTypes.IDENTIFIER }
            ?.text

    override fun setName(name: String) = throw UnsupportedOperationException()

    fun getNameIdentifier(): PsiElement? = null
}

class WitPackageDeclNode(node: ASTNode) : WitElement(node)

class WitInterfaceNode(node: ASTNode) : WitNamedDeclNode(node)

class WitWorldNode(node: ASTNode) : WitNamedDeclNode(node)
