package witx.surface.psi.nodes

import witx.surface.psi.WitxElement
import witx.surface.psi.WitxTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil

open class WitxNamedDeclNode(node: ASTNode) : WitxElement(node), PsiNamedElement {
    override fun getName(): String? =
        PsiTreeUtil.findChildrenOfType(this, com.intellij.psi.PsiElement::class.java)
            .firstOrNull { it.node?.elementType == WitxTypes.IDENTIFIER }
            ?.text

    override fun setName(name: String) = throw UnsupportedOperationException()

    fun getNameIdentifier(): PsiElement? = null
}

class WitxPackageDeclNode(node: ASTNode) : WitxElement(node)

class WitxInterfaceNode(node: ASTNode) : WitxNamedDeclNode(node)

class WitxWorldNode(node: ASTNode) : WitxNamedDeclNode(node)
