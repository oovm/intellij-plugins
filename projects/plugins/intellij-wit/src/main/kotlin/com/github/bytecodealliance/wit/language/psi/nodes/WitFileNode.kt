package com.github.bytecodealliance.wit.language.psi.nodes

import com.github.bytecodealliance.wit.WitLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider

class WitFileNode(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WitLanguage) {
    override fun toString(): String = "WitFile"
    override fun getFileType() = com.github.bytecodealliance.wit.language.file.WitFileType.INSTANCE
}
