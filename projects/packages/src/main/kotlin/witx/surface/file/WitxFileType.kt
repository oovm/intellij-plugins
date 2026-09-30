package witx.surface.file

import witx.definition.WitxBundle
import witx.definition.WitxLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WitxFileType private constructor() : LanguageFileType(WitxLanguage) {
    override fun getName(): String = "WITX"

    override fun getDescription(): String = WitxBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "wit"

    override fun getIcon(): Icon = WitxIcons.FILE

    companion object {
        @JvmField
        val INSTANCE = WitxFileType()
    }
}
