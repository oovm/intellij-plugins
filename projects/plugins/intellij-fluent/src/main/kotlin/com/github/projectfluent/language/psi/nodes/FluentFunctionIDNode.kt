package com.github.projectfluent.language.psi.nodes

import com.github.projectfluent.language.psi.FluentElement
import com.github.projectfluent.language.psi.FluentVisitor
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor

class FluentFunctionIDNode(node: ASTNode) : FluentElement(node) {
    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is FluentVisitor) {
            visitor.visitFunctionID(this)
        } else {
            super.accept(visitor)
        }
    }
}
