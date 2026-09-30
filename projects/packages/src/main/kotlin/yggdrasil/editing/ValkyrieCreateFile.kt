package yggdrasil.editing.actions

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog.Builder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import yggdrasil.definition.YggdrasilBundle
import yggdrasil.surface.file.YggdrasilIconProvider

class ValkyrieCreateFile :
    CreateFileFromTemplateAction(
        { YggdrasilBundle.message("action.create_file") },
        { YggdrasilBundle.message("action.create_file.description") },
        YggdrasilIconProvider.Instance.Yggdrasil,
    ) {
    companion object {
        // See [resources/colors/fileTemplate]
        private const val templatePath = "Valkyrie File"
    }

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: Builder) {
        builder.setTitle(YggdrasilBundle.message("action.create_file"))
            .addKind("Empty file", YggdrasilIconProvider.Instance.Yggdrasil, templatePath)
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
        YggdrasilBundle.message("action.create_file")
}
