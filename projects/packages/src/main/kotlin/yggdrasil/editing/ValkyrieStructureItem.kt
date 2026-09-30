package yggdrasil.editing.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import yggdrasil.surface.ast.traversal
import javax.swing.Icon

class ValkyrieStructureItem : StructureViewTreeElement, SortableTreeElement {
    val node: NavigatablePsiElement
    val view: ItemPresentation;
    var visibility: ValkyrieVisibility = ValkyrieVisibility.Public

    constructor(self: NavigatablePsiElement) {
        this.node = self
        this.view = self.presentation!!
    }

    constructor(self: NavigatablePsiElement, name: String, icon: Icon) {
        this.node = self
        this.view = PresentationData(name, "", icon, null)
    }

    override fun getValue(): Any = node

    override fun navigate(requestFocus: Boolean) = node.navigate(requestFocus)

    override fun canNavigate(): Boolean = node.canNavigate()

    override fun canNavigateToSource(): Boolean = node.canNavigateToSource()

    override fun getAlphaSortKey(): String = node.name ?: ""

    override fun getPresentation(): ItemPresentation = view

    override fun getChildren(): Array<out ValkyrieStructureItem> = findChildrenView(node)

    companion object {
        fun findChildrenView(root: PsiElement): Array<ValkyrieStructureItem> {
            val output = mutableListOf<ValkyrieStructureItem>();
            root.traversal {
                if (it is NavigatablePsiElement) {
                    if (it.presentation != null) {
                        output.add(ValkyrieStructureItem(it))
                        false
                    } else {
                        true;
                    }
                } else {
                    true
                }
            }
            return output.toTypedArray()
        }
    }
}
