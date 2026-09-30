package yggdrasil.editing.structure

import com.intellij.ide.structureView.*
import com.intellij.ide.structureView.StructureViewModel.ElementInfoProvider
import com.intellij.ide.structureView.StructureViewModel.ExpandInfoProvider
import com.intellij.ide.util.treeView.smartTree.Filter
import com.intellij.ide.util.treeView.smartTree.Grouper
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class YggdrasilStructureView : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): Builder {
        return Builder(psiFile)
    }

    class Builder(private val file: PsiFile) : TreeBasedStructureViewBuilder() {

        override fun createStructureViewModel(editor: Editor?): StructureViewModel {
            return Model(file)
        }

        override fun createStructureView(fileEditor: FileEditor?, project: Project): StructureView {
            return super.createStructureView(fileEditor, project)
        }

        override fun isRootNodeShown(): Boolean {
            return true;
        }
    }

    class Model(file: PsiFile?) : TextEditorBasedStructureViewModel(null, file), ElementInfoProvider, ExpandInfoProvider {
        override fun getRoot(): StructureViewTreeElement {
            return ValkyrieStructureItem(psiFile)
        }

        override fun getSorters() = arrayOf(Sorter.ALPHA_SORTER)

        override fun getFilters(): Array<Filter> {
            return super.getFilters()
        }

        override fun getGroupers(): Array<Grouper> {
            return super.getGroupers()
        }

        override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean {
            // 是否是叶节点
            return false
        }

        override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean {
            // 是否显示展开按钮
            return false
        }

        override fun getCurrentEditorElement(): Any? {
            return super.getCurrentEditorElement()
        }

        override fun isAutoExpand(element: StructureViewTreeElement): Boolean {
            return true
        }

        override fun isSmartExpand(): Boolean {
            return true
        }
    }

}

