package yggdrasil.semantic.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceService
import com.intellij.util.ProcessingContext

class ClassReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        println("getReferencesByElement($element, $context)")


//        PsiReferenceService.getService().getReferences(element, PsiReferenceService.Hints.NO_HINTS)
        TODO("Not yet implemented $element $context")
    }

    override fun acceptsHints(element: PsiElement, hints: PsiReferenceService.Hints): Boolean {
        TODO("acceptsHints $element $hints")
    }

    override fun acceptsTarget(target: PsiElement): Boolean {
        TODO("acceptsTarget $target")
    }
}