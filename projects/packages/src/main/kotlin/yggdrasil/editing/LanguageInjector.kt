package yggdrasil.editing.assist


import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.psi.PsiElement
import yggdrasil.surface.psi.nodes.YggdrasilRegexNode

class LanguageInjector : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        when (context) {
            is YggdrasilRegexNode -> {
                // TODO: Implement regex injection
            }
        }
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(YggdrasilRegexNode::class.java)
    }
}

