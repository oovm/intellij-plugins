package vos.editing.highlight

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import vos.surface.file.VosFileNode
import vos.surface.psi.VosAnnotationNode
import vos.surface.psi.VosClassFieldNode
import vos.surface.psi.VosClassStatementNode
import vos.surface.psi.VosKvPairNode
import vos.surface.psi.VosModifiersNode
import vos.surface.psi.VosSchemaStatementNode
import vos.surface.psi.VosTypeSymbolNode
import vos.surface.psi.VosTypes
import vos.surface.psi.VosUnionFieldNode
import vos.surface.psi.VosUnionStatementNode
import vos.surface.psi.VosValueNode

class HighlightAST : HighlightVisitor {
    private var infoHolder: HighlightInfoHolder? = null

    override fun visit(element: PsiElement) {
        when (element) {
            is VosSchemaStatementNode -> {
                val head = element.firstChild
                if (head != null) {
                    highlight(head, VosColor.KEYWORD)
                    val prop = head.nextLeaf { it.elementType == VosTypes.SYMBOL }
                    if (prop != null) {
                        highlight(prop, VosColor.SYM_SCHEMA)
                    }
                }
            }
            is VosTypeSymbolNode -> {
                val head = element.text.firstOrNull()
                if (head != null && head.isLowerCase()) {
                    highlight(element, VosColor.KEYWORD)
                } else {
                    highlight(element, VosColor.SYM_CLASS)
                }
            }
            is VosAnnotationNode -> highlight(element, VosColor.SYM_ANNO)
            is VosClassStatementNode -> {
                val id = element.identifier
                val head = id.text.firstOrNull()
                if (head != null && head.isLowerCase()) {
                    highlight(id, VosColor.KEYWORD)
                } else {
                    highlight(id, VosColor.SYM_CLASS)
                }
            }
            is VosClassFieldNode -> highlight(element.identifier, VosColor.SYM_FIELD)
            is VosUnionStatementNode -> highlight(element.identifier, VosColor.SYM_CLASS)
            is VosUnionFieldNode -> highlight(element.identifier, VosColor.SYM_FIELD)
            is VosKvPairNode -> {
                val head = element.firstChild
                if (head != null) highlight(head, VosColor.SYM_FIELD)
            }
            is VosValueNode -> {
                when (element.firstChild?.elementType) {
                    VosTypes.NULL -> highlight(element.firstChild!!, VosColor.NULL)
                    VosTypes.BOOLEAN -> highlight(element.firstChild!!, VosColor.BOOLEAN)
                }
            }
            is VosModifiersNode -> {
                for (child in element.children) {
                    highlight(child, VosColor.MODIFIER)
                }
            }
        }
    }

    private fun highlight(element: PsiElement, color: VosColor) {
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

    override fun suitableForFile(file: PsiFile): Boolean = file is VosFileNode
}
