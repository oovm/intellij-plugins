package vos.editing.structure.filter


import com.intellij.icons.AllIcons
import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import vos.editing.structure.ViewElement
import vos.surface.file.MessageBundle


object MainInfoFilter : Filter {

    override fun getName() = "action.view.filter.main_info"

    override fun isReverted() = true
    override fun getPresentation(): ActionPresentation = ActionPresentationData(
        MessageBundle.message(name),
        null,
        AllIcons.Nodes.Favorite
    )

    override fun isVisible(node: TreeElement): Boolean {
        return (node as? ViewElement)?.getVisibility() ?: true
    }
}
