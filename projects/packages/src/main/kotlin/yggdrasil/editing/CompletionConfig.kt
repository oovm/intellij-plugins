package yggdrasil.editing.completion

import com.intellij.application.options.CodeCompletionOptionsCustomSection
import javax.swing.JComponent

private val TITLE = yggdrasil.definition.YggdrasilBundle.message("settings.completion.title")

class CompletionConfig : CodeCompletionOptionsCustomSection {
    init {
//        checkBox(
//            ValkyrieBundle.message("settings.completion.suggest_out_of_scope_items"),
//            CodeInsightSettings.getInstance()::suggestOutOfScopeItems
//        )
    }

    override fun createComponent(): JComponent? {
        return null;
    }

    override fun isModified(): Boolean {
        return false;
    }

    override fun apply() {

    }
}