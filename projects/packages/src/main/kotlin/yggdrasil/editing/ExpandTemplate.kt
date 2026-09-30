package yggdrasil.editing.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory

private val name = yggdrasil.definition.YggdrasilBundle.message("action.macro.expand_file")
private val description = yggdrasil.definition.YggdrasilBundle.message("action.convert_prop.description")

class ExpandTemplate : AnAction(name, description, AllIcons.Actions.GroupByMethod) {
    override fun actionPerformed(e: AnActionEvent) {
//        val src = LangDataKeys.PSI_FILE.getData(e.dataContext) ?: return
//        if (isValidFile(src)) {
//            val file = WriteAction.compute<PsiFile, RuntimeException> {
//                createFile(src.containingDirectory, "${src.virtualFile.nameWithoutExtension}.g.vk", src.text)
//            }
//            FileTypeUsageCounterCollector.triggerCreate(file.project, file.virtualFile)
//        }
//        return arrayOf<PsiElement>(file)
    }

    private fun isValidFile(file: PsiFile): Boolean {
        if (file.isDirectory) return false
        if (file.virtualFile.name.endsWith(".g.vk")) return false
        return true
    }

    private fun createFile(dir: PsiDirectory, fileName: String, text: String): PsiFile? {
        val builder = PsiFileFactory.getInstance(dir.project);
        for (child in dir.files) {
            if (child.name == fileName) {
                child.delete()
                break
            }
        }
        val file = builder.createFileFromText(fileName, yggdrasil.definition.YggdrasilLanguage, text)
        dir.add(file)
        return file;
    }
}