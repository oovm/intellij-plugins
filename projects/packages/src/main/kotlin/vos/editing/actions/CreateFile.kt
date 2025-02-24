package vos.editing.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog.Builder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import vos.surface.file.MessageBundle
import vos.surface.file.VosIcons

class CreateFile : CreateFileFromTemplateAction(name, description, VosIcons.FILE) {
    companion object {
        private val name = MessageBundle.message("action.create_file")
        private val description = MessageBundle.message("action.create_file.description")
    }


    override fun buildDialog(project: Project, directory: PsiDirectory, builder: Builder) {
        builder
            .setTitle(name)
            // See [resources/colors/fileTemplate]
            .addKind("Empty file", VosIcons.FILE, "Empty Object")
    }


    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String = name
}
