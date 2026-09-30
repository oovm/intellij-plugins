package yggdrasil.surface.psi.nodes

import com.intellij.lang.ASTNode
import yggdrasil.surface.psi.YggdrasilDeclaration
import yggdrasil.surface.psi.YggdrasilTypes

class YggdrasilDefineClassNode(node: ASTNode) : YggdrasilDeclaration(node) {
    override fun getNameIdentifier(): YggdrasilIdentifierNode? {
        return findChildByType(YggdrasilTypes.IDENTIFIER) as? YggdrasilIdentifierNode
    }

    override fun getBaseIcon(): javax.swing.Icon {
        return com.intellij.icons.AllIcons.Nodes.Class
    }
}
