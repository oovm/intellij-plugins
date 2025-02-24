//package vos.editing.completion
//
//import com.intellij.codeInsight.completion.CompletionParameters
//import com.intellij.codeInsight.completion.CompletionProvider
//import com.intellij.codeInsight.completion.CompletionResultSet
//import com.intellij.codeInsight.lookup.LookupElementBuilder
//import com.intellij.openapi.editor.EditorModificationUtil
//import com.intellij.util.ProcessingContext
//import djv.intellij.language.file.DjvIcon
//import javax.swing.Icon
//
//class TextCompletionProvider : CompletionProvider<CompletionParameters>() {
//    override fun addCompletions(
//        parameters: CompletionParameters, context: ProcessingContext, resultSet: CompletionResultSet
//    ) {
//        addSlot(
//            resultSet, "for-in-slot", "for i in items", """
//                            {% for i in body %}
//
//                            {% end %}
//                            """
//        )
//        addInline(
//            resultSet, "for-in-inline", "for i in items", """
//                            {% for i in body {} %}
//                            """
//        )
//        addSlot(
//            resultSet, "if-slot", "if", """
//                            {% if condition %}
//
//                            {% end %}
//                            """
//        )
//        addInline(
//            resultSet, "if-inline", "if", """
//                            {% if condition {} %}
//                            """
//        )
//    }
//
//    private fun addSlot(
//        resultSet: CompletionResultSet,
//        keys: String,
//        showText: String,
//        insertText: String,
//    ) {
//        addElement(resultSet, keys, "{% $showText %}", insertText, icon = DjvIcon.SLOT)
//    }
//    private fun addInline(
//        resultSet: CompletionResultSet,
//        keys: String,
//        showText: String,
//        insertText: String,
//    ) {
//        addElement(resultSet, keys, showText, insertText, icon = DjvIcon.INLINE)
//    }
//
//    private fun addElement(
//        resultSet: CompletionResultSet,
//        keys: String,
//        showText: String,
//        insertText: String,
//        typeText: String? = null,
//        tailText: String? = null,
//        icon: Icon? = null,
//    ) {
//        resultSet.addElement(LookupElementBuilder.create(keys).withCaseSensitivity(false).withPresentableText(showText)
//            .withTailText(tailText, true).withTypeText(typeText).withIcon(icon).withInsertHandler { ctx, _ ->
//                EditorModificationUtil.moveCaretRelatively(ctx.editor, -keys.length)
//                EditorModificationUtil.insertStringAtCaret(
//                    ctx.editor, insertText.trimIndent()
//                )
//            })
//    }
//}