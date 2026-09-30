package yggdrasil.definition

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import yggdrasil.surface.psi.YggdrasilLexer


private const val LANGUAGE_ID = "yggdrasil"


object YggdrasilLanguage : Language(LANGUAGE_ID) {
    override fun getID(): String {
        return LANGUAGE_ID
    }

    override fun getDisplayName(): String {
        return "Yggdrasil"
    }

    override fun getAssociatedFileType(): LanguageFileType? {
        return super.getAssociatedFileType()
    }

    override fun getMimeTypes(): Array<String> {
        return arrayOf("text/x-yggdrasil")
    }
}