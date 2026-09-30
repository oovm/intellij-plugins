package yggdrasil.editing.completion

import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement


class TemplateReplaceElement(val element: PsiElement, val id: String, val text: String, val discard: Map<String, String>) :
    InsertHandler<LookupElement> {
    override fun handleInsert(context: InsertionContext, item: LookupElement) {
        context.document.replaceString(context.startOffset, context.tailOffset, "")
        TemplateBuilder(element, context.editor).runFromText(id, text, discard)
    }

    companion object {
        fun snippetFromPath(
            element: PsiElement,
            id: String,
            file: String,
            discard: Map<String, String> = mapOf(),
        ): LookupElementBuilder {
            val path = "/templates/liveTemplate/$file";
            val text = object {}.javaClass.getResourceAsStream(path)?.use { it.reader(Charsets.UTF_8).readText() } ?: path
            val insert = TemplateReplaceElement(element, id, text, discard);
            return LookupElementBuilder.create(id).withInsertHandler(insert)
        }
    }
}


