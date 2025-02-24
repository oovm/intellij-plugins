package vos.editing.completion

import vos.surface.psi.VosTypes
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.patterns.PlatformPatterns

class CompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(VosTypes.SYMBOL),
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
