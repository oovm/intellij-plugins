package vos.editing.structure


import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import vos.surface.ast.DeclareNode
import vos.surface.file.VosFileNode

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
            is VosFileNode -> self.getChildrenView()
            is DeclareNode -> self.getChildrenView()
            else -> arrayOf()
        }
    }

    // TODO: return object
    fun getVisibility(): Boolean = true
}