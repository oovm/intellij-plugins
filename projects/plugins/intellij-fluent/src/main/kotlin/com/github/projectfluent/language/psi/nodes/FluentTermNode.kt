package com.github.projectfluent.language.psi.nodes

import com.github.projectfluent.language.psi.FluentElement
import com.github.projectfluent.language.psi.FluentVisitor
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class FluentTermNode(node: ASTNode) : FluentElement(node) {
    val termID: FluentTermIDNode
        get() = findChildByClass(FluentTermIDNode::class.java) ?: throw IllegalStateException("Term must have an ID")

    fun getNameIdentifier(): PsiElement? {
        return termID
    }

    fun setName(name: String): PsiElement {
        return this
    }

    override fun getName(): String? {
        return termID.text
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is FluentVisitor) {
            visitor.visitTerm(this)
        } else {
            super.accept(visitor)
        }
    }
}
