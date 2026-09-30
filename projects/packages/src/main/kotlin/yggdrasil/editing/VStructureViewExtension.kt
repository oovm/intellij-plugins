package yggdrasil.editing.structure

import com.intellij.ide.structureView.StructureViewExtension
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class VStructureViewExtension : StructureViewExtension {
    override fun getType(): Class<out PsiElement> {
        TODO("Not yet implemented")
    }

    override fun getChildren(parent: PsiElement?): Array<StructureViewTreeElement> {
        return arrayOf()
    }

    override fun getCurrentEditorElement(editor: Editor?, parent: PsiElement?): Any? {
        return null
    }
}