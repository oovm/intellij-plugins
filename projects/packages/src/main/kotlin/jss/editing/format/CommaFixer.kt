package jss.editing.format


import com.intellij.lang.SmartEnterProcessorWithFixers
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class CommaFixer : SmartEnterProcessorWithFixers.Fixer<SmartEnter>() {
    override fun apply(editor: Editor, processor: SmartEnter, element: PsiElement) {

    }
}
