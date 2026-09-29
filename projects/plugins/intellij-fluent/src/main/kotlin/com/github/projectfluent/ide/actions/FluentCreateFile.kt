package com.github.projectfluent.ide.actions

import com.github.projectfluent.language.file.FluentIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog.Builder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class FluentCreateFile :
    CreateFileFromTemplateAction(com.github.projectfluent.FluentBundle.message("action.create_file"), com.github.projectfluent.FluentBundle.message("action.create_file.description"), com.github.projectfluent.language.file.FluentIcons.FILE) {
    companion object {
        // See [resources/colors/fileTemplate]
        private const val templatePath = "Fluent File";
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: Builder) {
        builder.setTitle(com.github.projectfluent.FluentBundle.message("action.create_file")).addKind("Empty file", FluentIcons.FILE!!, templatePath)
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String = com.github.projectfluent.FluentBundle.message("action.create_file")
}
