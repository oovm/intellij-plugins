package fluent.plugin.integration

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.Language
import fluent.definition.FluentLanguage
import org.intellij.plugins.markdown.injection.CodeFenceLanguageProvider

/**
 * Registered only from [plugin-with-markdown.xml] when the Markdown plugin is present.
 */
class FluentMarkdownFenceLanguageProvider : CodeFenceLanguageProvider {
    override fun getLanguageByInfoString(infoString: String): Language? =
        when (infoString.trim().lowercase()) {
            "ftl", "fluent" -> FluentLanguage
            else -> null
        }

    override fun getCompletionVariantsForInfoString(parameters: CompletionParameters): List<LookupElement> =
        listOf(
            LookupElementBuilder.create("ftl"),
            LookupElementBuilder.create("fluent"),
        )
}
