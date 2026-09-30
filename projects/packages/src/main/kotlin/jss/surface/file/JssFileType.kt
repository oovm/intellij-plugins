package jss.surface.file

import com.intellij.openapi.fileTypes.LanguageFileType
import jss.surface.file.JssLanguage
import javax.swing.Icon

object JssFileType : LanguageFileType(JssLanguage) {
    override fun getName(): String = JssLanguage.id

    override fun getDescription(): String = MessageBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "vos"

    override fun getIcon(): Icon = JssIcons.FILE
}