package yggdrasil.surface.psi.nodes

import com.intellij.lang.ASTNode
import yggdrasil.surface.psi.YggdrasilElement

class YggdrasilNumberNode(node: ASTNode) : YggdrasilElement(node) {
    val numberText: String
        get() = node.text
}
