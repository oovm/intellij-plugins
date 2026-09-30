package yggdrasil.editing.assist

import com.intellij.application.options.editor.CodeFoldingOptionsProvider
import javax.swing.JComponent

/// 显示在 Editor > General > Code Folding 中
class ValkyrieFoldingOptions : CodeFoldingOptionsProvider {
    override fun createComponent(): JComponent? {
        return null
    }

    override fun isModified(): Boolean {
        return false;
    }

    override fun apply() {

    }
}