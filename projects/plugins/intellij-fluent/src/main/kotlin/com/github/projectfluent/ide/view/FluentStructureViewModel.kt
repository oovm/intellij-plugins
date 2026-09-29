package com.github.projectfluent.ide.view

import com.github.projectfluent.language.psi.nodes.FluentMessageNode
import com.github.projectfluent.language.psi.nodes.FluentAttributeNode
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.psi.PsiFile

class FluentStructureViewModel(psiFile: PsiFile?) :
    StructureViewModelBase(psiFile!!, FluentStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {
    override fun getSorters(): Array<Sorter> {
        return arrayOf(Sorter.ALPHA_SORTER)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
        return false
    }

    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
        return element.value is FluentAttributeNode
    }
}

