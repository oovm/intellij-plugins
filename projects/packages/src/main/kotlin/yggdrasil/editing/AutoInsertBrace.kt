package yggdrasil.editing.completion

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import yggdrasil.surface.file.YggdrasilFileType

class AutoInsertBrace : TypedHandlerDelegate() {
    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (file.fileType !is YggdrasilFileType) {
            return Result.CONTINUE
        }
        val caretOffset = editor.caretModel.offset
        when (c) {
            '(' -> {
                editor.document.insertString(caretOffset, ")")
                editor.caretModel.moveToOffset(caretOffset)
                return Result.STOP
            }

            '[' -> {
                editor.document.insertString(caretOffset, "]")
                editor.caretModel.moveToOffset(caretOffset);
                return Result.STOP
            }

            '{' -> {
                editor.document.insertString(caretOffset, "}")
                editor.caretModel.moveToOffset(caretOffset);
                return Result.STOP
            }
        }
        return Result.CONTINUE
    }
}