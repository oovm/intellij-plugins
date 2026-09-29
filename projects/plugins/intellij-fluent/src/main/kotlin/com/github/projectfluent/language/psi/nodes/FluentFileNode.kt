package com.github.projectfluent.language.psi.nodes

import com.github.projectfluent.FluentLanguage
import com.github.projectfluent.language.file.FluentFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class FluentFileNode(view: FileViewProvider) : PsiFileBase(view, FluentLanguage) {
    override fun getFileType(): FileType = FluentFileType.INSTANCE

    override fun toString(): String = "FluentFileNode"
}