package jss.editing.structure


import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import jss.surface.ast.DeclareNode
import jss.surface.file.JssFileNode

class ViewElement(private val self: NavigatablePsiElement) :
    StructureViewTreeElement, SortableTreeElement {
    override fun getValue(): Any = self

    override fun navigate(requestFocus: Boolean) = self.navigate(requestFocus)

    override fun canNavigate(): Boolean = self.canNavigate()

    override fun canNavigateToSource(): Boolean = self.canNavigateToSource()

    override fun getAlphaSortKey(): String = self.name ?: ""

    override fun getPresentation(): ItemPresentation = self.presentation ?: PresentationData()

    override fun getChildren(): Array<out TreeElement> {
        return when (self) {
            is JssFileNode -> self.getChildrenView()
            is DeclareNode -> self.getChildrenView()
            else -> arrayOf()
        }
    }

    // TODO: return object
    fun getVisibility(): Boolean = true
}