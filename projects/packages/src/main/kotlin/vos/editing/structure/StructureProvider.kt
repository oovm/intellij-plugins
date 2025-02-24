package vos.editing.structure


import com.intellij.ide.projectView.SelectableTreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.ExternalLibrariesNode
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl


class StructureProvider : SelectableTreeStructureProvider, DumbAware {
    override fun modify(
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        settings: ViewSettings?,
    ): List<AbstractTreeNode<out Any>> = children.map {
        when (it) {
            is PsiFileNode -> CustomFileWrapper(it, settings)
            is PsiDirectoryNode -> it
            is ExternalLibrariesNode -> it
            else -> it
        }
    }

    override fun getTopLevelElement(element: PsiElement?): PsiElement? = when (element) {
        is PsiWhiteSpaceImpl, is PsiWhiteSpace, is LeafPsiElement -> {
            null
        }
//        is ValkyrieFile -> {
//            null
//        }
        else -> null
    }

    private class CustomFileWrapper(var original: PsiFileNode, viewSettings: ViewSettings?) :
        PsiFileNode(original.project, original.value, viewSettings) {
        override fun getSortKey(): Int {
            val file = original.value;
            return when {
                file.name == "readme.md" -> {
                    -9
                }
//                file is ValkyrieFile && file.isIndexFile() -> {
//                    -8
//                }
                else -> 0;
            }
        }
    }
}

