package wit.surface.psi

import wit.surface.psi.nodes.WitFileNode
import wit.surface.psi.nodes.WitInterfaceNode
import wit.surface.psi.nodes.WitPackageDeclNode
import wit.surface.psi.nodes.WitWorldNode
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
