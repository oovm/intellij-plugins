package vos.surface.ast

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.ide.projectView.PresentationData
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiWhiteSpace
import vos.editing.structure.ViewElement
import vos.surface.psi.searchChildrenOfType
import javax.swing.Icon

abstract class DeclareNode(node: ASTNode) : ASTWrapperPsiElement(node),
    PsiNameIdentifierOwner {

    abstract override fun getOriginalElement(): PsiElement;

    abstract override fun getNameIdentifier(): PsiElement;

    override fun getName(): String {
        return nameIdentifier.text
    }

    abstract override fun setName(name: String): PsiElement;

    override fun getNavigationElement(): PsiElement {
        return nameIdentifier
    }

    override fun getTextOffset(): Int {
        return nameIdentifier.textOffset
    }

    override fun getPresentation(): ItemPresentation = PresentationData(
        this.name,
        "",
        this.getIcon(0),
        null
    )

    abstract override fun getIcon(flags: Int): Icon;

    open fun getChildrenView(): Array<ViewElement> {
        return arrayOf()
    }

    open fun findDocumentation(): Array<PsiComment> {
        val out = mutableListOf<PsiComment>()
        var now = this.prevSibling
        while (now != null) {
            when (now) {
                is PsiComment -> when {
                    now.text.startsWith("///") -> out.add(now)
                    else -> break
                }
                is PsiWhiteSpace -> continue
                else -> break
            }
            now = now.prevSibling
        }
        return out.toTypedArray()
    }
}