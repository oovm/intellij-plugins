package com.github.projectfluent.ide.formatter

import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class FluentCommaFixer : SmartEnterProcessorWithFixers.Fixer<FluentSmartEnter>() {
    override fun apply(editor: Editor, processor: FluentSmartEnter, element: PsiElement) {
        TODO("Not yet implemented")
    }
}
