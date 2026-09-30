package fluent.surface.ast

import fluent.definition.FluentLanguage
import fluent.surface.psi.nodes.FluentFileNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

class ASTNodeFactory(private val project: Project) {
    fun createFile(text: String): FluentFileNode {
        val file =  PsiFileFactory.getInstance(project).createFileFromText("dummy.fluent", FluentLanguage, text)
        return file as FluentFileNode
    }
}