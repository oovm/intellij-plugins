package jss.surface.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

open class JssNode(node: ASTNode) : ASTWrapperPsiElement(node) {
    protected fun token(type: IElementType): PsiElement? = findChildByType(type)
}
