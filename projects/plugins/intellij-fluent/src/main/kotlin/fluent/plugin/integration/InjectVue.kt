package fluent.plugin.integration

import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText
import fluent.definition.FluentLanguage

class InjectVue : MultiHostInjector {
    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context !is XmlText) {
            return
        }
        val host = context as? PsiLanguageInjectionHost ?: return
        if (!isFluentTagContent(context)) {
            return
        }
        registrar.startInjecting(FluentLanguage)
        registrar.addPlace(null, null, host, TextRange(0, context.textLength))
        registrar.doneInjecting()
    }

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return mutableListOf(XmlText::class.java)
    }

    companion object {
        internal fun isFluentTagContent(context: PsiElement): Boolean {
            val tag = context.parent as? XmlTag ?: return false
            return tag.localName.equals("fluent", ignoreCase = true)
        }
    }
}
