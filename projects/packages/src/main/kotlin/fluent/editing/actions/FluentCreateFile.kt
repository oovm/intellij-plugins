package fluent.editing.actions

import fluent.surface.file.FluentIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog.Builder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class FluentCreateFile :
    CreateFileFromTemplateAction(
        fluent.definition.FluentBundle.message("action.create_file"),
        fluent.definition.FluentBundle.message("action.create_file.description"),
        FluentIcons.FILE,
    ) {
    companion object {
        // See [resources/colors/fileTemplate]
        private const val templatePath = "Fluent File"
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: Builder) {
        builder.setTitle(fluent.definition.FluentBundle.message("action.create_file"))
            .addKind("Empty file", FluentIcons.FILE, templatePath)
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
        fluent.definition.FluentBundle.message("action.create_file")
}
