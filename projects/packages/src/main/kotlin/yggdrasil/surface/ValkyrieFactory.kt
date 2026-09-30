package yggdrasil.surface.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import yggdrasil.surface.psi.nodes.YggdrasilNumberNode

class ValkyrieFactory {
    private val project: Project

    constructor(project: Project) {
        this.project = project
    }


    constructor(element: PsiElement) {
        this.project = element.project
    }

    fun createNumberLiteral(number: String, unit: String?): YggdrasilNumberNode {
//        return letDefineAtom(number)!!.number as ValkyrieNumberNode
        throw Exception("unreachable: ValkyrieFactory::createNumberLiteral")
    }
//
//    private fun letDefineAtom(atom: String): ValkyrieAtom? {
//        val file = createFile("let target = $atom;");
//        for (child in file.children) {
//            if (child is ValkyrieLetStatementNode) {
//                return child.expression!!.termList.first().atom
//            }
//        }
//        return null
//    }
}