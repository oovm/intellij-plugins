package com.github.projectfluent.ide.formatter

import com.github.projectfluent.language.psi.FluentTypes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class FluentFormatBuilder : FormattingModelBuilder {
    override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? {
        // Expand to the enclosing message/term so selection reformat has full indent context (#5).
        var node = elementAtOffset ?: return null
        while (node.treeParent != null) {
            val type = node.elementType
            if (type == FluentTypes.MESSAGE || type == FluentTypes.TERM) {
                return node.textRange
            }
            node = node.treeParent
        }
        return elementAtOffset.textRange
    }

    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val element = formattingContext.psiElement
        val ctx = FluentFormatSpace.create(settings)
        val block = FluentFormatBlock(element.node, null, Indent.getNoneIndent(), null, ctx)
        return FormattingModelProvider.createFormattingModelForPsiFile(element.containingFile, block, settings)
    }
}
