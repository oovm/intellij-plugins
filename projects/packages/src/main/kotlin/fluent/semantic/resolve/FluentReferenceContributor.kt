package fluent.semantic.resolve

import fluent.surface.psi.FluentTypes
import fluent.surface.psi.nodes.FluentAttributeIDNode
import fluent.surface.psi.nodes.FluentMessageIDNode
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class FluentReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        // 注册消息引用提供者
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(FluentMessageIDNode::class.java),
            FluentReferenceProvider()
        )
        
        // 注册属性引用提供者
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(FluentAttributeIDNode::class.java),
            FluentReferenceProvider()
        )
    }
}
