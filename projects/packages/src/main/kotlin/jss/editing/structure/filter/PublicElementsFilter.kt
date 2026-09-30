package jss.editing.structure.filter


import jss.editing.structure.ViewElement
import com.intellij.icons.AllIcons
import com.intellij.ide.util.treeView.smartTree.ActionPresentation
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import jss.surface.file.MessageBundle


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
