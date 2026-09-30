package jss.editing.completion

import jss.surface.psi.JssTypes
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns

class CompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(JssTypes.SYMBOL),
            SymbolProvider()
        )
//        extend(
//            CompletionType.BASIC,
//            PlatformPatterns.psiElement(JssTypes.IDIOM_MARK),
//            IdiomProvider()
//        )
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        super.beforeCompletion(context)
    }
}
