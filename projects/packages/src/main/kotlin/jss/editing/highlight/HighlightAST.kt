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
import jss.surface.psi.VosAnnotationNode
import jss.surface.psi.VosClassFieldNode
import jss.surface.psi.VosClassStatementNode
import jss.surface.psi.JssKvPairNode
import jss.surface.psi.VosModifiersNode
import jss.surface.psi.JssSchemaStatementNode
import jss.surface.psi.JssTypeSymbolNode
import jss.surface.psi.JssTypes
import jss.surface.psi.VosUnionFieldNode
import jss.surface.psi.VosUnionStatementNode
import jss.surface.psi.VosValueNode

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
            is VosAnnotationNode -> highlight(element, JssColor.SYM_ANNO)
            is VosClassStatementNode -> {
                val id = element.identifier
                val head = id.text.firstOrNull()
                if (head != null && head.isLowerCase()) {
                    highlight(id, JssColor.KEYWORD)
                } else {
                    highlight(id, JssColor.SYM_CLASS)
                }
            }
            is VosClassFieldNode -> highlight(element.identifier, JssColor.SYM_FIELD)
            is VosUnionStatementNode -> highlight(element.identifier, JssColor.SYM_CLASS)
            is VosUnionFieldNode -> highlight(element.identifier, JssColor.SYM_FIELD)
            is JssKvPairNode -> {
                val head = element.firstChild
                if (head != null) highlight(head, JssColor.SYM_FIELD)
            }
            is VosValueNode -> {
                when (element.firstChild?.elementType) {
                    JssTypes.NULL -> highlight(element.firstChild!!, JssColor.NULL)
                    JssTypes.BOOLEAN -> highlight(element.firstChild!!, JssColor.BOOLEAN)
                }
            }
            is VosModifiersNode -> {
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
