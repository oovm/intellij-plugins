package yggdrasil.editing.navigation

import com.intellij.codeInsight.navigation.GotoTargetHandler
import com.intellij.codeInsight.navigation.actions.GotoSuperAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import yggdrasil.surface.psi.nodes.YggdrasilDefineClass

//import nexus.language.psi_node.ValkyrieClassStatementNode
//import nexus.language.psi_node.ValkyrieTraitStatementNode

class GotoSuper : GotoTargetHandler() {
    override fun getFeatureUsedKey() = GotoSuperAction.FEATURE_ID
    override fun getSourceAndTargetElements(editor: Editor, file: PsiFile): GotoData? {
        val source = findSource(editor, file) ?: return null
        val targets = findTargets(source)
        return GotoData(source, targets.toTypedArray(), emptyList())
    }

    override fun getChooserTitle(sourceElement: PsiElement, name: String?, length: Int, finished: Boolean) = when (sourceElement) {
        is YggdrasilDefineClass -> "ValkyrieClassStatementNode"
        else -> "???????"
        // EditorConfigBundle.get("goto.super.select.parent")
    }

    override fun getNotFoundMessage(project: Project, editor: Editor, file: PsiFile) = when (findSource(editor, file)) {
        is YggdrasilDefineClass -> "ValkyrieClassStatementNode not found"
        else -> "???? not found"
    }
}

private fun findSource(editor: Editor, file: PsiFile): PsiElement? {
    val element = file.findElementAt(editor.caretModel.offset) ?: return null
    return PsiTreeUtil.getParentOfType(
        element,
        YggdrasilDefineClass::class.java,
    )
}

private fun findTargets(element: PsiElement): List<PsiElement?> = listOf(
    PsiTreeUtil.getParentOfType(
        element,
        YggdrasilDefineClass::class.java,
    )
)