package com.github.bytecodealliance.wit.language.file

import com.github.bytecodealliance.wit.WitBundle
import com.github.bytecodealliance.wit.WitLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class WitFileType private constructor() : LanguageFileType(WitLanguage) {
    override fun getName(): String = "WIT"

    override fun getDescription(): String = WitBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "wit"

    override fun getIcon(): Icon = WitIcons.FILE

    companion object {
        @JvmField
        val INSTANCE = WitFileType()
    }
}
