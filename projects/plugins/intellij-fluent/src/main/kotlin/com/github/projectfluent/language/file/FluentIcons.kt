package com.github.projectfluent.language.file

import com.intellij.ide.IconProvider
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import javax.swing.Icon

class FluentIcons : IconProvider() {
    override fun getIcon(psiElement: PsiElement, flags: Int): Icon? {
        val fileName = psiElement.containingFile.name

        return when {
            fileName.endsWith(".ftl") -> FILE
            else -> null
        }
    }

    companion object {
        var FILE: Icon? = IconLoader.getIcon("/icons/ftl.svg", FluentIcons::class.java)
    }
}