package vos.editing.structure


import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile
import vos.editing.structure.filter.MainInfoFilter
import vos.editing.structure.filter.PublicElementsFilter
import vos.editing.structure.grouper.PropertiesGrouper


class ViewModel(psiFile: PsiFile?) : StructureViewModelBase(psiFile!!, ViewElement(psiFile)),
    ElementInfoProvider {
    override fun getSorters() = arrayOf(Sorter.ALPHA_SORTER)

    override fun getFilters() = arrayOf(PublicElementsFilter, MainInfoFilter)

    override fun getGroupers() = arrayOf(PropertiesGrouper)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        return false
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        // return element.value is ValkyrieBitflagStatement
        return false
    }
}

