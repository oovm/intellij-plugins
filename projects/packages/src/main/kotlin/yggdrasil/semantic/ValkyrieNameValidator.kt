package yggdrasil.semantic.inspect

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project

class ValkyrieNameValidator : NamesValidator {
    override fun isKeyword(name: String, project: Project?): Boolean {
        return true
    }

    override fun isIdentifier(name: String, project: Project?): Boolean {
        return !isKeyword(name, project)
    }
}
