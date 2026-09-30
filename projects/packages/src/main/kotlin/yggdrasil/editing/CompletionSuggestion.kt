package yggdrasil.editing.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.SuggestedNameInfo
import com.intellij.refactoring.rename.NameSuggestionProvider

class CompletionSuggestion : NameSuggestionProvider {
    override fun getSuggestedNames(element: PsiElement, nameSuggestionContext: PsiElement?, result: MutableSet<String>): SuggestedNameInfo? {
        println(element)
        println(nameSuggestionContext)
        println(result)
        return null
    }
}