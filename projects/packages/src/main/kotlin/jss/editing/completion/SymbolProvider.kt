package jss.editing.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.util.ProcessingContext
import jss.editing.completion.lookup.PropertyData
import jss.surface.file.JssIcons

class SymbolProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
        val kind = SchemaCompletionContext.collectionKind(parameters.position)
        val existing = SchemaCompletionContext.existingPropertyKeys(parameters.position)
        addProperty(resultSet, kind, existing)
    }

    private fun addProperty(set: CompletionResultSet, kind: SchemaCollectionKind, existing: Set<String>) {
        set.addKeyword("property", "keyword")
        set.addKeyword("schema", "keyword")
        when (kind) {
            SchemaCollectionKind.OBJECT -> {
                set.addProperty("type", "string")
                PropertyData.objectCompletion(set, existing)
            }
            SchemaCollectionKind.ARRAY -> {
                set.addProperty("type", "string")
                PropertyData.arrayCompletion(set, existing)
                addArrayProperty(set, existing)
            }
            SchemaCollectionKind.UNKNOWN -> {
                set.addProperty("type", "string | array")
                set.addProperty("allOf", "array")
                set.addProperty("anyOf", "array")
                set.addProperty("oneOf", "array")
                addStringProperty(set, existing)
                addNumberProperty(set, existing)
            }
        }
    }

    private fun addArrayProperty(set: CompletionResultSet, existing: Set<String>) {
        set.addProperty("minItems", "number", existing)
        set.addProperty("maxItems", "number", existing)
        set.addProperty("uniqueItems", "boolean", existing)
    }

    private fun addStringProperty(set: CompletionResultSet, existing: Set<String>) {
        set.addProperty("minLength", "number", existing)
        set.addProperty("maxLength", "number", existing)
        set.addProperty("pattern", "regex", existing)
    }

    private fun addNumberProperty(set: CompletionResultSet, existing: Set<String>) {
        set.addProperty("minimum", "number", existing)
        set.addProperty("exclusiveMinimum", "boolean", existing)
        set.addProperty("maximum", "number", existing)
        set.addProperty("exclusiveMaximum", "boolean", existing)
        set.addProperty("multipleOf", "boolean", existing)
    }
}

private fun CompletionResultSet.addProperty(field: String, typing: String, existing: Set<String> = emptySet()) {
    if (field in existing) return
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
        .withIcon(JssIcons.PROPERTY)
        .withTypeText(typing)
    this.addElement(e2)
}

private fun CompletionResultSet.addKeyword(keyword: String, typing: String) {
    val e = LookupElementBuilder.create("$keyword {}")
        .withPresentableText(keyword)
        .withIcon(JssIcons.SCHEMA)
        .withTypeText(typing)
        .withBoldness(true)
    this.addElement(e)
}
