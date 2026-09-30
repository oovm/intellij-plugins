package fluent.surface.file

import fluent.definition.FluentBundle
import fluent.definition.FluentLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class FluentFileType private constructor() : LanguageFileType(FluentLanguage) {
    override fun getName(): String = FluentLanguage.id

    override fun getDescription(): String = FluentBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "ftl"

    override fun getIcon(): Icon = FluentIcons.FILE

    companion object {
        @JvmStatic
        val INSTANCE = FluentFileType()
    }
}