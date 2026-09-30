package yggdrasil.editing.completion

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import yggdrasil.surface.file.YggdrasilFileType

class AutoDeleteBrace : BackspaceHandlerDelegate() {
    override fun beforeCharDeleted(c: Char, file: PsiFile, editor: Editor) {

    }

    override fun charDeleted(c: Char, file: PsiFile, editor: Editor): Boolean {

        if (file.fileType !is YggdrasilFileType) {
            return false;
        }
        return false;
//        val caretOffset = editor.caretModel.offset
//        val deltaOffset = editor.document.charsSequence.indexOfFirst { !it.isWhitespace() }
//        if (deltaOffset == -1) {
//            return false;
//        }
//        val rightOffset = caretOffset + deltaOffset - 1;
//        val rightChar = editor.document.charsSequence[rightOffset];
//        println("删除: ${rightChar} at ${rightOffset}")
//
//
//        if (c == '(') {
//            if (rightChar == ')') {
//                editor.document.deleteString(caretOffset, rightOffset + 1)
//                editor.caretModel.moveToOffset(caretOffset)
//                return true
//            }
//            return false
//        }
//
//        return false
    }
}