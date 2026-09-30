package yggdrasil.surface.psi.nodes

import com.intellij.lang.ASTNode
import yggdrasil.surface.psi.YggdrasilElement

class YggdrasilIdentifierNode(node: ASTNode) : YggdrasilElement(node) {
    val identifierText: String
        get() = node.text
}
