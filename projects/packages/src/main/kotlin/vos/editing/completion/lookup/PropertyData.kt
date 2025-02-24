package vos.editing.completion.lookup

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import vos.surface.file.VosIcons


object PropertyData {
    fun objectCompletion(set: CompletionResultSet, excepts: Set<String>) {
        PropertyDataBase.INSTANCE.objectSet
            .filter { it.key !in excepts }
            .forEach { set.addElement(it.value) }
    }
    fun objectCheck(name: String): Boolean {
        return PropertyDataBase.INSTANCE.objectSet.containsKey(name)
    }
    fun arrayCompletion(set: CompletionResultSet, excepts: Set<String>) {
        PropertyDataBase.INSTANCE.arraySet
            .filter { it.key !in excepts }
            .forEach { set.addElement(it.value) }
    }
}

private class PropertyDataBase {
    init {
        buildObjectSet()
        buildArraySet()
    }

    val objectSet = mutableMapOf<String, LookupElement>()
    val arraySet = mutableMapOf<String, LookupElement>()

    private fun buildObjectSet() {
        addProperty(objectSet, "required", "array")
        addProperty(objectSet, "properties", "object")
        addProperty(objectSet, "patternProperties", "object")
        addProperty(objectSet, "additionalProperties", "object")
        addProperty(objectSet, "minProperties", "number")
        addProperty(objectSet, "maxProperties", "number")
    }

    private fun buildArraySet() {
        addProperty(arraySet, "items", "object")
        addProperty(arraySet, "additionalItems", "object")
        addProperty(arraySet, "minItems", "number")
        addProperty(arraySet, "maxItems", "number")
    }

    private fun addProperty(set: MutableMap<String, LookupElement>, field: String, typing: String) {
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
        set[field] = e2
    }

    companion object {
        val INSTANCE: PropertyDataBase = PropertyDataBase()
    }
}