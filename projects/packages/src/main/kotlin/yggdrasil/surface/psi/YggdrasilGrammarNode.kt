package yggdrasil.surface.psi.nodes

import com.intellij.lang.ASTNode
import yggdrasil.surface.psi.YggdrasilElement
import yggdrasil.surface.psi.YggdrasilTypes

class YggdrasilGrammarNode(node: ASTNode) : YggdrasilElement(node) {
    val grammarName: String?
        get() = node.findChildByType(YggdrasilTypes.IDENTIFIER)?.text
}
