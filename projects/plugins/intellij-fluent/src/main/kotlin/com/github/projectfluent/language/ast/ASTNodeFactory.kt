package com.github.projectfluent.language.ast

import com.github.projectfluent.FluentLanguage
import com.github.projectfluent.language.psi.nodes.FluentFileNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

class ASTNodeFactory(private val project: Project) {
    fun createFile(text: String): FluentFileNode {
        val file =  PsiFileFactory.getInstance(project).createFileFromText("dummy.fluent", FluentLanguage, text)
        return file as FluentFileNode
    }
}