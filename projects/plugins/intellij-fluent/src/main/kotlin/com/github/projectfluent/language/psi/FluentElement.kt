package com.github.projectfluent.language.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

open class FluentElement(node: ASTNode) : ASTWrapperPsiElement(node) {
    override fun toString(): String {
        return super.toString()
    }
}