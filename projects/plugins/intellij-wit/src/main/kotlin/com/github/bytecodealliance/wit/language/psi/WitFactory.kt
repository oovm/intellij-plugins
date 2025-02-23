package com.github.bytecodealliance.wit.language.psi

import com.github.bytecodealliance.wit.language.psi.nodes.WitFileNode
import com.github.bytecodealliance.wit.language.psi.nodes.WitInterfaceNode
import com.github.bytecodealliance.wit.language.psi.nodes.WitPackageDeclNode
import com.github.bytecodealliance.wit.language.psi.nodes.WitWorldNode
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

object WitFactory {
    fun createElement(node: ASTNode): PsiElement =
        when (node.elementType) {
            WitTypes.PACKAGE_DECL -> WitPackageDeclNode(node)
            WitTypes.INTERFACE_DECL -> WitInterfaceNode(node)
            WitTypes.WORLD_DECL -> WitWorldNode(node)
            else -> WitElement(node)
        }
}
