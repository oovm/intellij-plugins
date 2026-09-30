package yggdrasil.semantic.resolve

import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class ValkyrieReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
//        registrar.registerReferenceProvider(
//            PlatformPatterns.psiElement(ValkyrieIdentifierNode::class.java),
//            ClassReferenceProvider()
//        )
    }
}

