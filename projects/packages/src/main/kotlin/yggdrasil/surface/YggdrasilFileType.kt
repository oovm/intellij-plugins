package yggdrasil.surface.file

import com.intellij.openapi.fileTypes.LanguageFileType
import yggdrasil.definition.YggdrasilLanguage
import javax.swing.Icon

object YggdrasilFileType : LanguageFileType(YggdrasilLanguage) {
    override fun getName(): String = YggdrasilLanguage.id

    override fun getDescription(): String = yggdrasil.definition.YggdrasilBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "ygg;yggdrasil;"

    override fun getIcon(): Icon = YggdrasilIconProvider.Instance.Yggdrasil

}