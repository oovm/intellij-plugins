package fluent.surface.psi.nodes

import fluent.definition.FluentLanguage
import fluent.surface.file.FluentFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class FluentFileNode(view: FileViewProvider) : PsiFileBase(view, FluentLanguage) {
    override fun getFileType(): FileType = FluentFileType.INSTANCE

    override fun toString(): String = "FluentFileNode"
}