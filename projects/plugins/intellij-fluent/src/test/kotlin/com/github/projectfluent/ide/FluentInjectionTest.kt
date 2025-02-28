package com.github.projectfluent.ide

import com.github.projectfluent.FluentLanguage
import com.github.projectfluent.ide.highlight.FluentMarkdownFenceLanguageProvider
import com.github.projectfluent.ide.highlight.InjectVue
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlText
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class FluentInjectionTest : BasePlatformTestCase() {
    fun testInjectVueTargetsFluentXmlTag() {
        myFixture.configureByText(
            "fluent-block.xml",
            """
            <fluent locale="en">
            hello = Hello, world!
            </fluent>
            """.trimIndent(),
        )
        val xmlText = PsiTreeUtil.findChildOfType(myFixture.file, XmlText::class.java)
        assertNotNull(xmlText)
        assertTrue(InjectVue.isFluentTagContent(xmlText!!))
    }

    fun testInjectVueIgnoresOtherXmlTags() {
        myFixture.configureByText(
            "other-block.xml",
            """
            <template>
            hello = Hello, world!
            </template>
            """.trimIndent(),
        )
        val xmlText = PsiTreeUtil.findChildOfType(myFixture.file, XmlText::class.java)
        assertNotNull(xmlText)
        assertFalse(InjectVue.isFluentTagContent(xmlText!!))
    }

    fun testInjectVueTagNameIsCaseInsensitive() {
        myFixture.configureByText(
            "fluent-block.xml",
            """
            <Fluent locale="en">
            title = Example
            </Fluent>
            """.trimIndent(),
        )
        val xmlText = PsiTreeUtil.findChildOfType(myFixture.file, XmlText::class.java)
        assertNotNull(xmlText)
        assertTrue(InjectVue.isFluentTagContent(xmlText!!))
    }

    fun testInjectVueRegistersFluentLanguage() {
        myFixture.configureByText(
            "fluent-block.xml",
            """
            <fluent>
            hello = Hello!
            </fluent>
            """.trimIndent(),
        )
        val xmlText = PsiTreeUtil.findChildOfType(myFixture.file, XmlText::class.java) as XmlText
        val host = xmlText as PsiLanguageInjectionHost
        val registrar = CapturingRegistrar()
        InjectVue().getLanguagesToInject(registrar, xmlText)
        assertEquals(FluentLanguage, registrar.language)
        assertEquals(1, registrar.places.size)
        assertEquals(TextRange(0, xmlText.textLength), registrar.places.single().range)
        assertSame(host, registrar.places.single().host)
    }

    fun testMarkdownFenceProviderMapsFluentAliases() {
        val provider = FluentMarkdownFenceLanguageProvider()
        assertEquals(FluentLanguage, provider.getLanguageByInfoString("ftl"))
        assertEquals(FluentLanguage, provider.getLanguageByInfoString("fluent"))
        assertEquals(FluentLanguage, provider.getLanguageByInfoString("  FTL  "))
        assertNull(provider.getLanguageByInfoString("json"))
    }

    private class CapturingRegistrar : MultiHostRegistrar {
        var language: com.intellij.lang.Language? = null
        val places = mutableListOf<InjectedPlace>()

        override fun startInjecting(language: com.intellij.lang.Language): MultiHostRegistrar {
            this.language = language
            return this
        }

        override fun addPlace(
            prefix: String?,
            suffix: String?,
            host: PsiLanguageInjectionHost,
            range: TextRange,
        ): MultiHostRegistrar {
            places += InjectedPlace(host, range)
            return this
        }

        override fun doneInjecting() = Unit

        data class InjectedPlace(val host: PsiLanguageInjectionHost, val range: TextRange)
    }
}
