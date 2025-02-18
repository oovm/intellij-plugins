package com.github.projectfluent.language.psi.nodes

import com.github.projectfluent.language.psi.FluentElement
import com.github.projectfluent.language.psi.FluentVisitor
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class FluentAttributeNode(node: ASTNode) : FluentElement(node) {
    val attributeID: FluentAttributeIDNode
        get() = findChildByClass(FluentAttributeIDNode::class.java) ?: throw IllegalStateException("Attribute must have an ID")

    fun getNameIdentifier(): PsiElement? {
        return attributeID
    }

    fun setName(name: String): PsiElement {
        return this
    }

    override fun getName(): String? {
        return attributeID.text
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is FluentVisitor) {
            visitor.visitAttribute(this)
        } else {
            super.accept(visitor)
        }
    }
}
