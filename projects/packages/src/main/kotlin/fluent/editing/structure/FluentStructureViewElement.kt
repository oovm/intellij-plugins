package fluent.editing.structure

import fluent.surface.file.FluentIcons
import fluent.surface.psi.nodes.FluentAttributeNode
import fluent.surface.psi.nodes.FluentFileNode
import fluent.surface.psi.nodes.FluentMessageNode
import fluent.surface.psi.nodes.FluentTermNode
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.icons.AllIcons

/**
 * Structure view for Fluent files: messages, terms, and nested attributes (#7).
 */
class FluentStructureViewElement(private val node: NavigatablePsiElement) :
    StructureViewTreeElement,
    SortableTreeElement {
    override fun getValue(): Any = node

    override fun navigate(requestFocus: Boolean) = node.navigate(requestFocus)

    override fun canNavigate(): Boolean = node.canNavigate()

    override fun canNavigateToSource(): Boolean = node.canNavigateToSource()

    override fun getAlphaSortKey(): String = node.name.orEmpty()

    override fun getPresentation(): ItemPresentation {
        val name = when (node) {
            is FluentFileNode -> node.name
            is FluentTermNode -> "-${node.name}"
            is FluentAttributeNode -> node.name
            else -> node.name
        }.orEmpty()
        val icon = when (node) {
            is FluentFileNode -> FluentIcons.FILE
            is FluentTermNode -> AllIcons.Nodes.Constant
            is FluentAttributeNode -> AllIcons.Nodes.Property
            is FluentMessageNode -> AllIcons.Nodes.Property
            else -> null
        }
        return PresentationData(name, null, icon, null)
    }

    override fun getChildren(): Array<out TreeElement> = when (node) {
        is FluentFileNode ->
            node.children.mapNotNull { child ->
                when (child) {
                    is FluentMessageNode, is FluentTermNode -> FluentStructureViewElement(child)
                    else -> null
                }
            }.toTypedArray()

        is FluentMessageNode, is FluentTermNode ->
            PsiTreeUtil.getChildrenOfTypeAsList(node, FluentAttributeNode::class.java)
                .map { FluentStructureViewElement(it) }
                .toTypedArray()

        else -> emptyArray()
    }
}
