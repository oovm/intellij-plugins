package com.github.projectfluent.ide.highlight


import com.github.projectfluent.language.psi.FluentTypes
import com.github.projectfluent.language.psi.FluentRecursiveVisitor
import com.github.projectfluent.language.psi.nodes.FluentAttributeIDNode
import com.github.projectfluent.language.psi.nodes.FluentFileNode
import com.github.projectfluent.language.psi.nodes.FluentFunctionIDNode
import com.github.projectfluent.language.psi.nodes.FluentMessageIDNode
import com.github.projectfluent.language.psi.nodes.FluentTermIDNode
import com.github.projectfluent.language.psi.nodes.FluentVariableIDNode
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class FluentSemanticHighlighter : FluentRecursiveVisitor(), HighlightVisitor {
    private var infoHolder: HighlightInfoHolder? = null

    override fun visitElement(element: PsiElement) {
        when (element.node.elementType) {
            FluentTypes.INLINE_TEXT -> {
                val childTokenType = element.firstChild?.node?.elementType
                if (
                    childTokenType != FluentTypes.STRING_QUOTE &&
                    childTokenType != FluentTypes.STRING_CHAR &&
                    childTokenType != FluentTypes.STRING_ESCAPE
                ) {
                    highlight(element, FluentHighlightColor.TEXT)
                }
            }
            FluentTypes.VARIANT_KEY -> highlight(element, FluentHighlightColor.SYM_TERM)
        }
        super.visitElement(element)
    }

    override fun visitMessageID(messageID: FluentMessageIDNode) {
        val parent = messageID.parent
        if (parent.node.elementType == FluentTypes.MESSAGE) {
            // 消息定义
            highlight(messageID, FluentHighlightColor.SYM_MESSAGE)
        } else if (parent.node.elementType == FluentTypes.SYMBOL_REFERENCE) {
            // 消息引用
            highlight(messageID, FluentHighlightColor.SYM_MESSAGE_REFERENCE)
        }
    }

    override fun visitTermID(termID: FluentTermIDNode) {
        highlight(termID, FluentHighlightColor.SYM_TERM)
    }

    override fun visitAttributeID(attributeID: FluentAttributeIDNode) {
        val parent = attributeID.parent
        if (parent.node.elementType == FluentTypes.ATTRIBUTE) {
            // 属性定义
            highlight(attributeID, FluentHighlightColor.SYM_ATTRIBUTE)
        } else if (parent.node.elementType == FluentTypes.SYMBOL_REFERENCE) {
            // 属性引用
            highlight(attributeID, FluentHighlightColor.SYM_ATTRIBUTE_REFERENCE)
        }
    }

    override fun visitVariableID(variableID: FluentVariableIDNode) {
        highlight(variableID, FluentHighlightColor.SYM_VARIABLE)
    }

    override fun visitFunctionID(functionID: FluentFunctionIDNode) {
        highlight(functionID, FluentHighlightColor.SYM_FUNCTION)
    }

    private fun highlight(element: PsiElement, color: FluentHighlightColor) {
        val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
        builder.textAttributes(color.textAttributesKey)
        builder.range(element)

        infoHolder?.add(builder.create())
    }

    override fun analyze(file: PsiFile, whole: Boolean, holder: HighlightInfoHolder, action: Runnable): Boolean {
        infoHolder = holder
        action.run()
        visit(file)

        return true
    }

    override fun clone(): HighlightVisitor = FluentSemanticHighlighter()

    override fun suitableForFile(file: PsiFile): Boolean = file is FluentFileNode

    override fun visit(element: PsiElement) {
        element.accept(this)
    }
}
