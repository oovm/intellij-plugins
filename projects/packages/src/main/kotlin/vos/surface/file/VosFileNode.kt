package vos.surface.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import vos.editing.structure.ViewElement
import vos.surface.file.VosLanguage
import vos.surface.ast.DeclareNode
import vos.surface.psi.searchChildrenOfType

class VosFileNode(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, VosLanguage) {
    override fun getFileType(): FileType = VosFileType

    override fun toString(): String = MessageBundle.message("filetype.description")
    
    fun getChildrenView(): Array<ViewElement> {
        return this.searchChildrenOfType(DeclareNode::class.java)
            .map { ViewElement(it) }
            .toTypedArray()
    }
}


