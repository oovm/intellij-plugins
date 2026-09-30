package yggdrasil.editing.inlay

import com.intellij.codeInsight.hints.VcsCodeVisionLanguageContext
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import yggdrasil.surface.psi.nodes.YggdrasilDefineClass
import yggdrasil.surface.psi.nodes.YggdrasilDefineUnion
import java.awt.event.MouseEvent

@Suppress("UnstableApiUsage")
class AuthorAbove : VcsCodeVisionLanguageContext {
    /// 额外的点击事件
    /// 本身会打开 Git Blame
    override fun handleClick(mouseEvent: MouseEvent, editor: Editor, element: PsiElement) {

    }

    override fun isAccepted(element: PsiElement): Boolean {
        return element is YggdrasilDefineClass
            || element is YggdrasilDefineUnion
//            || element is YggdrasilClimbStatement
//            || element is YggdrasilGrammarNode
    }

    override fun isCustomFileAccepted(file: PsiFile): Boolean {
        return false
    }
}


