package wit.surface.psi.nodes

import wit.surface.psi.WitElement
import wit.surface.psi.WitTypes
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil

open class WitNamedDeclNode(node: ASTNode) : WitElement(node), PsiNamedElement {
    override fun getName(): String? =
        PsiTreeUtil.findChildrenOfType(this, com.intellij.psi.PsiElement::class.java)
            .firstOrNull { it.node?.elementType == WitTypes.IDENTIFIER }
            ?.text

    override fun setName(name: String) = throw UnsupportedOperationException()

    fun getNameIdentifier(): PsiElement? = null
}

class WitPackageDeclNode(node: ASTNode) : WitElement(node)

class WitInterfaceNode(node: ASTNode) : WitNamedDeclNode(node)

class WitWorldNode(node: ASTNode) : WitNamedDeclNode(node)
