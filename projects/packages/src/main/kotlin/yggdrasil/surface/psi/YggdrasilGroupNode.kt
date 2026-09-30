package yggdrasil.surface.psi.nodes

import com.intellij.lang.ASTNode
import yggdrasil.surface.psi.YggdrasilElement
import yggdrasil.surface.psi.YggdrasilDeclaration

class YggdrasilGroupNode(node: ASTNode) : YggdrasilElement(node) {
    val tokenList: List<YggdrasilDeclaration>
        get() = children.filterIsInstance<YggdrasilDeclaration>()
}
