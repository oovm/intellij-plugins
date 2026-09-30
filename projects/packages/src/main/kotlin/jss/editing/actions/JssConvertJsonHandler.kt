package jss.editing.actions

import jss.surface.file.JssFileType
import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.json.psi.JsonFile
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.util.IncorrectOperationException


class JssConvertJsonHandler : CodeInsightActionHandler {
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val document = tryGetJsonSchema(file) ?: return;
        val buffer = StringBuilder()
        buffer.append(
            """${document.propertyList}
"""
        )
        try {
            PsiFileFactory.getInstance(file.project).createFileFromText(file.name, JssFileType, buffer)
        } catch (e: IncorrectOperationException) {
            // do nothing
        }
    }


    companion object {
        fun tryGetJsonSchema(file: PsiFile): JsonObject? {
            if (file is JsonFile) {
                when (val root = file.topLevelValue) {
                    is JsonObject -> {
                        if (root.findProperty("\$schema") != null) {
                            return root
                        }
                    }
                }
            }
            return null
        }
    }

}
