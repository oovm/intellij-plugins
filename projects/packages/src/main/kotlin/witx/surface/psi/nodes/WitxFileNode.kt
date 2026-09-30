package witx.surface.psi.nodes

import witx.surface.file.WitxFileType
import witx.definition.WitxLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class WitxFileNode(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WitxLanguage) {
    override fun toString(): String = "WitxFile"
    override fun getFileType() = WitxFileType.INSTANCE
}
