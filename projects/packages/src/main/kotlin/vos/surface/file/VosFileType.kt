package vos.surface.file

import com.intellij.openapi.fileTypes.LanguageFileType
import vos.surface.file.VosLanguage
import javax.swing.Icon

object VosFileType : LanguageFileType(VosLanguage) {
    override fun getName(): String = VosLanguage.id

    override fun getDescription(): String = MessageBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "vos"

    override fun getIcon(): Icon = VosIcons.FILE
}