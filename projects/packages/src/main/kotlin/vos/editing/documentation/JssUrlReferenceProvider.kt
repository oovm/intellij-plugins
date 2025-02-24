package vos.editing.documentation


import com.intellij.openapi.paths.WebReference
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext

class JssUrlReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
//        val literal = element as? JssUrlMaybeValid ?: return emptyArray()
//        val value = literal.url.text
//
//        if (value.startsWith("http://") || value.startsWith("https://")) {
//            return arrayOf(WebReference(element, value))
//        }
        return emptyArray()
    }
}