@file:Suppress("ConvertSecondaryConstructorToPrimary", "MemberVisibilityCanBePrivate", "UnstableApiUsage")

package yggdrasil.semantic.symbol

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.model.psi.PsiCompletableReference
import com.intellij.model.psi.PsiSymbolDeclaration
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import yggdrasil.surface.psi.nodes.YggdrasilIdentifierNode


class ImportData : Symbol, Pointer<ImportData>, PsiSymbolDeclaration, PsiCompletableReference {
    val nav: YggdrasilIdentifierNode
    var name: String
    var namepath: Array<String>

    constructor(nav: YggdrasilIdentifierNode, name: String) {
        this.nav = nav
        this.name = name
        this.namepath = arrayOf()
    }

    constructor(nav: YggdrasilIdentifierNode, name: String, namepath: Array<String>) {
        this.nav = nav
        this.name = name
        this.namepath = namepath
    }

    override fun createPointer(): Pointer<out ImportData> = this
    override fun dereference(): ImportData = this
    override fun getDeclaringElement(): PsiElement = nav
    override fun getRangeInDeclaringElement(): TextRange = nav.textRange
    override fun getElement(): PsiElement = nav
    override fun getRangeInElement(): TextRange = nav.textRange
    override fun getAbsoluteRange(): TextRange = nav.textRange
    override fun resolveReference(): MutableCollection<out Symbol> {
        TODO("Not yet implemented")
    }

    override fun getCompletionVariants(): MutableCollection<LookupElement> {
        TODO("Not yet implemented")
    }

    override fun getSymbol(): Symbol = this


    companion object {

    }


}