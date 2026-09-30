package fluent.editing.format

import fluent.surface.psi.FluentTypes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.lang.ASTNode
import com.intellij.lang.injection.InjectedLanguageManager
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
        val file = element.containingFile
        val injected = InjectedLanguageManager.getInstance(element.project).isInjectedFragment(file)
        // Injected Fluent already sits inside host indentation (#11).
        val rootIndent = if (injected) {
            Indent.getAbsoluteNoneIndent()
        } else {
            Indent.getNoneIndent()
        }
        val block = FluentFormatBlock(element.node, null, rootIndent, null, ctx)
        return FormattingModelProvider.createFormattingModelForPsiFile(file, block, settings)
    }
}
