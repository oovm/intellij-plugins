package vos.editing.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.util.ProcessingContext
import vos.editing.completion.lookup.PropertyData
import vos.surface.file.VosIcons

class SymbolProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
        print(parameters)
        print(context)

        addProperty(resultSet)
    }

    // FIXME: Remove fields that have already appeared
    private fun addProperty(set: CompletionResultSet) {
        set.addKeyword("property", "keyword")
        set.addKeyword("schema", "keyword")
        set.addProperty("type", "string | array")
        set.addProperty("allOf", "array")
        set.addProperty("anyOf", "array")
        set.addProperty("oneOf", "array")
        addArrayProperty(set)
        PropertyData.objectCompletion(set, setOf())
        addStringProperty(set)
        addNumberProperty(set)
    }

    private fun addArrayProperty(set: CompletionResultSet) {
        set.addProperty("minItems", "number")
        set.addProperty("maxItems", "number")
        set.addProperty("minimum", "number")
        set.addProperty("exclusiveMinimum", "boolean")
        set.addProperty("maximum", "number")
        set.addProperty("exclusiveMaximum", "boolean")
        set.addProperty("items", "object")
        set.addProperty("uniqueItems", "boolean")
    }

    private fun addStringProperty(set: CompletionResultSet) {
        set.addProperty("minLength", "number")
        set.addProperty("maxLength", "number")
        set.addProperty("pattern", "regex")
    }

    private fun addNumberProperty(set: CompletionResultSet) {
        set.addProperty("minimum", "number")
        set.addProperty("exclusiveMinimum", "boolean")
        set.addProperty("maximum", "number")
        set.addProperty("exclusiveMaximum", "boolean")
        set.addProperty("multipleOf", "boolean")
    }
}

private fun CompletionResultSet.addProperty(field: String, typing: String) {
    val e = when (typing) {
        "array" -> LookupElementBuilder.create("$field: []").withInsertHandler { ctx, _ ->
            EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
        }
        "object" -> LookupElementBuilder.create("$field: {}").withInsertHandler { ctx, _ ->
            EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
        }
        "string", "regex" -> LookupElementBuilder.create("$field: ''").withInsertHandler { ctx, _ ->
            EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
        }
        else -> {
            LookupElementBuilder.create("$field: ")
        }
    }
    val e2 = e.withPresentableText(field)
        .withIcon(VosIcons.PROPERTY)
        .withTypeText(typing)
    this.addElement(e2)
}

private fun CompletionResultSet.addKeyword(keyword: String, typing: String) {
    val e = LookupElementBuilder.create("$keyword {}")
        .withPresentableText(keyword)
        .withIcon(VosIcons.SCHEMA)
        .withTypeText(typing)
        .withBoldness(true)
    this.addElement(e)
}