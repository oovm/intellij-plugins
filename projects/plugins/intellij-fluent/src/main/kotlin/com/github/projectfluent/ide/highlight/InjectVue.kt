package com.github.projectfluent.ide.highlight

import com.github.projectfluent.FluentLanguage
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText

class InjectVue : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context is XmlText) {
            val host = context as? PsiLanguageInjectionHost ?: return
            val tag = context.parent
            if (tag is XmlTag && tag.name == "fluent") {
                registrar.startInjecting(FluentLanguage)
                registrar.addPlace(null, null, host, TextRange(0, context.textLength))
                registrar.doneInjecting()
            }
        }
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(XmlText::class.java)
    }
}
