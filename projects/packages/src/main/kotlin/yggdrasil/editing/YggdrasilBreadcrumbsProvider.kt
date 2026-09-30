package yggdrasil.editing.structure

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import yggdrasil.definition.YggdrasilLanguage
import yggdrasil.surface.psi.nodes.YggdrasilDefineClassNode
import yggdrasil.surface.psi.nodes.YggdrasilDefineUnionNode
import yggdrasil.surface.psi.nodes.YggdrasilGrammarNode
import yggdrasil.surface.psi.nodes.YggdrasilGroupNode

class YggdrasilBreadcrumbsProvider : BreadcrumbsProvider {
    override fun getLanguages(): Array<Language> {
        return arrayOf(YggdrasilLanguage)
    }

    override fun acceptElement(element: PsiElement) = when (element) {
        is YggdrasilGrammarNode,
        is YggdrasilDefineClassNode,
        is YggdrasilGroupNode,
            -> {
            true
        }
        is YggdrasilDefineUnionNode -> {
            true
        }
        else -> {
            false
        }
    }

    override fun getElementInfo(element: PsiElement) = when (element) {
        is YggdrasilDefineClassNode -> {
            element.name ?: "?"
        }

        is YggdrasilDefineUnionNode -> {
            element.name ?: "?"
        }

        else -> {
            "?"
        }
    }
}
