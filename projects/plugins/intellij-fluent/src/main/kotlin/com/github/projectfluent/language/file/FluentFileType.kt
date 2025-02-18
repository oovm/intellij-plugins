package com.github.projectfluent.language.file

import com.github.projectfluent.FluentBundle
import com.github.projectfluent.FluentLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class FluentFileType private constructor() : LanguageFileType(FluentLanguage) {
    override fun getName(): String = FluentLanguage.id

    override fun getDescription(): String = FluentBundle.message("filetype.description")

    override fun getDefaultExtension(): String = "ftl"

    override fun getIcon(): Icon = FluentIcons.FILE!!

    companion object {
        @JvmStatic
        val INSTANCE = FluentFileType()
    }
}