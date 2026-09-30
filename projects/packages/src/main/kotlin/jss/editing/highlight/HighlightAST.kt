package jss.editing.highlight

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import jss.surface.file.JssFileNode
import jss.surface.psi.JssAnnotationNode
import jss.surface.psi.JssClassFieldNode
import jss.surface.psi.JssClassStatementNode
import jss.surface.psi.JssKvPairNode
import jss.surface.psi.JssModifiersNode
import jss.surface.psi.JssSchemaStatementNode
import jss.surface.psi.JssTypeSymbolNode
import jss.surface.psi.JssTypes
import jss.surface.psi.JssUnionFieldNode
import jss.surface.psi.JssUnionStatementNode
import jss.surface.psi.JssValueNode

class HighlightAST : HighlightVisitor {
    private var infoHolder: HighlightInfoHolder? = null

    override fun visit(element: PsiElement) {
        when (element) {
            is JssSchemaStatementNode -> {
                val head = element.firstChild
                if (head != null) {
                    highlight(head, JssColor.KEYWORD)
                    val prop = head.nextLeaf { it.elementType == JssTypes.SYMBOL }
                    if (prop != null) {
                        highlight(prop, JssColor.SYM_SCHEMA)
                    }
                }
            }
            is JssTypeSymbolNode -> {
                val head = element.text.firstOrNull()
                if (head != null && head.isLowerCase()) {
                    highlight(element, JssColor.KEYWORD)
                } else {
                    highlight(element, JssColor.SYM_CLASS)
                }
            }
            is JssAnnotationNode -> highlight(element, JssColor.SYM_ANNO)
            is JssClassStatementNode -> {
                val id = element.identifier
                val head = id.text.firstOrNull()
                if (head != null && head.isLowerCase()) {
                    highlight(id, JssColor.KEYWORD)
                } else {
                    highlight(id, JssColor.SYM_CLASS)
                }
            }
            is JssClassFieldNode -> highlight(element.identifier, JssColor.SYM_FIELD)
            is JssUnionStatementNode -> highlight(element.identifier, JssColor.SYM_CLASS)
            is JssUnionFieldNode -> highlight(element.identifier, JssColor.SYM_FIELD)
            is JssKvPairNode -> {
                val head = element.firstChild
                if (head != null) highlight(head, JssColor.SYM_FIELD)
            }
            is JssValueNode -> {
                when (element.firstChild?.elementType) {
                    JssTypes.NULL -> highlight(element.firstChild!!, JssColor.NULL)
                    JssTypes.BOOLEAN -> highlight(element.firstChild!!, JssColor.BOOLEAN)
                }
            }
            is JssModifiersNode -> {
                for (child in element.children) {
                    highlight(child, JssColor.MODIFIER)
                }
            }
        }
    }

    private fun highlight(element: PsiElement, color: JssColor) {
        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
        builder.textAttributes(color.textAttributesKey)
        builder.range(element)
        infoHolder?.add(builder.create())
    }

    override fun analyze(
        file: PsiFile,
        updateWholeFile: Boolean,
        holder: HighlightInfoHolder,
        action: Runnable,
    ): Boolean {
        infoHolder = holder
        action.run()
        return true
    }

    override fun clone(): HighlightVisitor = HighlightAST()

    override fun suitableForFile(file: PsiFile): Boolean = file is JssFileNode
}
