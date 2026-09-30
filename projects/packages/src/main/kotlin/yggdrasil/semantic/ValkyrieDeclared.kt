@file:Suppress("ConvertSecondaryConstructorToPrimary", "MemberVisibilityCanBePrivate", "UnstableApiUsage")

package yggdrasil.semantic.symbol

import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import yggdrasil.surface.psi.nodes.YggdrasilDefineClass


class ValkyrieDeclared(val target: YggdrasilDefineClass) : PsiSymbolDeclaration, PsiReference {
    val source: PsiElement? = null;
    override fun getDeclaringElement(): PsiElement {
        return target
    }

    override fun getRangeInDeclaringElement(): TextRange {
        TODO("Not yet implemented")
    }

    override fun getElement(): PsiElement {
        TODO("Not yet implemented")
    }

    override fun getRangeInElement(): TextRange {
        TODO("Not yet implemented")
    }

    override fun getAbsoluteRange(): TextRange {
        TODO("Not yet implemented")
    }

    override fun resolve(): PsiElement? {
        TODO("Not yet implemented")
    }

    override fun getCanonicalText(): String {
        TODO("Not yet implemented")
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        TODO("Not yet implemented")
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        TODO("Not yet implemented")
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        TODO("Not yet implemented")
    }

    override fun isSoft(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSymbol(): ValkyrieSymbol {
        return ValkyrieSymbol()
    }
}