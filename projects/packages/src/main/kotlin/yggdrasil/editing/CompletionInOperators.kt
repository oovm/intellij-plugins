package yggdrasil.editing.completion

import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.impl.source.tree.LeafPsiElement


private fun triggerCondition(): PsiElementPattern.Capture<LeafPsiElement> {
    return PlatformPatterns.psiElement(LeafPsiElement::class.java).withLanguage(yggdrasil.definition.YggdrasilLanguage);
}