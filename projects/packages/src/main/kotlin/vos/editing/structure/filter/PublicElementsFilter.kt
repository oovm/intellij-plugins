package vos.editing.structure.filter


import vos.editing.structure.ViewElement
import com.intellij.icons.AllIcons
import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import vos.surface.file.MessageBundle


object PublicElementsFilter : Filter {

    override fun getName() = "action.view.filter.public"

    override fun isReverted() = true
    override fun getPresentation(): ActionPresentation = ActionPresentationData(
        MessageBundle.message(name),
        null,
        AllIcons.Nodes.Public
    )
    override fun isVisible(treeNode: TreeElement): Boolean {
        return (treeNode as? ViewElement)?.getVisibility() ?: true
    }
}
