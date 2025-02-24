package vos.editing.structure.grouper

import com.intellij.icons.AllIcons
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.smartTree.*
import vos.surface.file.MessageBundle

object PropertiesGrouper : Grouper {
    override fun getName(): String = "action.view.grouper.properties"
    override fun getPresentation(): ActionPresentation = ActionPresentationData(
        MessageBundle.message(name),
        null,
        AllIcons.Nodes.AbstractClass
    )

    override fun group(parent: AbstractTreeNode<*>, children: MutableCollection<TreeElement>): MutableCollection<Group> {
        return arrayListOf()
    }
}