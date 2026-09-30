package witx.surface.psi

import witx.surface.psi.nodes.WitxFileNode
import witx.surface.psi.nodes.WitxInterfaceNode
import witx.surface.psi.nodes.WitxPackageDeclNode
import witx.surface.psi.nodes.WitxWorldNode
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

object WitxFactory {
    fun createElement(node: ASTNode): PsiElement =
        when (node.elementType) {
            WitxTypes.PACKAGE_DECL -> WitxPackageDeclNode(node)
            WitxTypes.INTERFACE_DECL -> WitxInterfaceNode(node)
            WitxTypes.WORLD_DECL -> WitxWorldNode(node)
            else -> WitxElement(node)
        }
}
