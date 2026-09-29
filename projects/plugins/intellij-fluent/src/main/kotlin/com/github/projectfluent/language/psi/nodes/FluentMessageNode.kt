package com.github.projectfluent.language.psi.nodes

import com.github.projectfluent.language.psi.FluentElement
import com.github.projectfluent.language.psi.FluentVisitor
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class FluentMessageNode(node: ASTNode) : FluentElement(node) {
    val messageID: FluentMessageIDNode
        get() = findChildByClass(FluentMessageIDNode::class.java) ?: throw IllegalStateException("Message must have an ID")

    fun getNameIdentifier(): PsiElement? {
        return messageID
    }

    fun setName(name: String): PsiElement {
        return this
    }

    override fun getName(): String? {
        return messageID.text
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is FluentVisitor) {
            visitor.visitMessage(this)
        } else {
            super.accept(visitor)
        }
    }
}
