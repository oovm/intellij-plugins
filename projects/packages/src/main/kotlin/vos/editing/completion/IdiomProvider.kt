package vos.editing.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.util.ProcessingContext

class IdiomProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet) {
        resultSet.addElement(LookupElementBuilder.create("*").bold())
        for ((key, value) in STANDARD_FUNCTIONS) {
            resultSet.addElement(
                LookupElementBuilder.create("$key()")
                    .withPresentableText(key)
                    .withIcon(AllIcons.Nodes.DataSchema)
                    .withTailText("()")
                    .withTypeText(value)
            )
        }
        resultSet.addElement(
            LookupElementBuilder.create("@include")
                .withInsertHandler { ctx, _ ->
                    EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
                }
        )
        resultSet.addElement(
            LookupElementBuilder.create("@inherit")
                .withInsertHandler { ctx, _ ->
                    EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
                }
        )
        resultSet.addElement(
            LookupElementBuilder.create("Some()")
                .withInsertHandler { ctx, _ ->
                    EditorModificationUtil.moveCaretRelatively(ctx.editor, -1)
                }
        )
        resultSet.addElement(LookupElementBuilder.create("None()"))
    }
}

private val STANDARD_FUNCTIONS: Map<String, String> = mapOf(
    "avg" to "number",
    "concat" to "string",
    "keys" to "array"
)