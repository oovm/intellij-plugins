package yggdrasil.semantic.resolve

import com.intellij.model.Symbol
import com.intellij.model.psi.ImplicitReferenceProvider
import com.intellij.psi.PsiElement

@Suppress("UnstableApiUsage")
class ValkyrieImplicitProvider : ImplicitReferenceProvider {
    override fun resolveAsReference(element: PsiElement): MutableCollection<out Symbol> {
        TODO("Not yet implemented $element")
    }
}