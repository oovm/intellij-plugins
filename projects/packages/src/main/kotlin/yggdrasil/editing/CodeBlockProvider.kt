package yggdrasil.editing.assist

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile


/**
 * Alt + Up/Down
 */
class CodeBlockProvider : com.intellij.codeInsight.editorActions.CodeBlockProvider {
    override fun getCodeBlockRange(editor: Editor?, psiFile: PsiFile?): TextRange? {
        print("getCodeBlockRange $editor $psiFile")
        return null
    }
}


