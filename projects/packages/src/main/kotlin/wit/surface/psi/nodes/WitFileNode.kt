package wit.surface.psi.nodes

import wit.surface.file.WitFileType
import wit.definition.WitLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class WitFileNode(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WitLanguage) {
    override fun toString(): String = "WitFile"
    override fun getFileType() = WitFileType.INSTANCE
}
