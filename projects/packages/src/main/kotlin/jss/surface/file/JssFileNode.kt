package jss.surface.file

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import jss.editing.structure.ViewElement
import jss.surface.file.JssLanguage
import jss.surface.ast.DeclareNode
import jss.surface.psi.searchChildrenOfType

class JssFileNode(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, JssLanguage) {
    override fun getFileType(): FileType = JssFileType

    override fun toString(): String = MessageBundle.message("filetype.description")
    
    fun getChildrenView(): Array<ViewElement> {
        return this.searchChildrenOfType(DeclareNode::class.java)
            .map { ViewElement(it) }
            .toTypedArray()
    }
}


