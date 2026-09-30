package yggdrasil.editing.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import yggdrasil.surface.file.YggdrasilFileNode

class CompletionInClassScope : CompletionProvider<CompletionParameters>() {
    var element: PsiElement? = null;
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        element = parameters.position
        result.addDefinitions()
        result.addKeywords("ANY", "HIDE", "true", "false")
        result.addConstant(
            "END_OF_FILE",
            "END_OF_LINE",
            "REST_OF_LINE",
        )
    }

    private fun CompletionResultSet.addDefinitions() {
        val file = element?.containingFile as? YggdrasilFileNode ?: return;
        file.definitions.getDefinitions().forEach {
            it.createLookup(this)
        }
    }


    private fun CompletionResultSet.addKeywords(vararg text: String) {
        for (s in text) {
            addElement(
                LookupElementBuilder.create(s).bold()
                    .withIcon(AllIcons.Actions.ProjectWideAnalysisOn)
            )
        }
    }

    private fun CompletionResultSet.addConstant(vararg texts: String) {
        for (s in texts) {

            addElement(
                LookupElementBuilder.create(s).bold()
                    .withIcon(AllIcons.Actions.ProjectWideAnalysisOff)
            )
        }
    }


}
