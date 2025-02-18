package com.github.projectfluent.ide.reference

import com.github.projectfluent.language.psi.FluentTypes
import com.github.projectfluent.language.psi.nodes.FluentAttributeIDNode
import com.github.projectfluent.language.psi.nodes.FluentMessageIDNode
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
