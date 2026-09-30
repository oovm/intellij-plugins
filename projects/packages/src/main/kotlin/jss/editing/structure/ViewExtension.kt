package jss.editing.structure

import com.intellij.ide.structureView.StructureViewExtension
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class ViewExtension : StructureViewExtension {
    override fun getType(): Class<out PsiElement> {
        TODO("Not yet implemented")
    }

    override fun getChildren(parent: PsiElement?): Array<StructureViewTreeElement> {
        TODO("Not yet implemented")
    }

    override fun getCurrentEditorElement(editor: Editor?, parent: PsiElement?): Any? {
        TODO("Not yet implemented")
    }
}