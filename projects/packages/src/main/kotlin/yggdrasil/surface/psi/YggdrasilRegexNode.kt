package yggdrasil.surface.psi.nodes

import com.intellij.lang.ASTNode
import yggdrasil.surface.psi.YggdrasilElement

class YggdrasilRegexNode(node: ASTNode) : YggdrasilElement(node) {
    val regexText: String
        get() = node.text
}
