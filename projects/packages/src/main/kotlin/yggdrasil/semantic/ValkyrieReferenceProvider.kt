package yggdrasil.semantic.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

@Suppress("UnstableApiUsage")
class ValkyrieReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        println("getReferencesByElement(${element}, ${context})")
        return arrayOf()
    }
}
