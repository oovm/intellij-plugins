package vos.editing.assist

import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import vos.surface.psi.VosArrayNode
import vos.surface.psi.VosBraceBlockNode
import vos.surface.psi.VosBracketBlockNode
import vos.surface.psi.VosClassBlockNode
import vos.surface.psi.VosObjectNode
import vos.surface.psi.VosUnionBlockNode

class FoldingVisitor(
    private val descriptors: MutableList<FoldingDescriptor>,
) : PsiRecursiveElementVisitor() {
    override fun visitElement(element: PsiElement) {
        when (element) {
            is VosBraceBlockNode, is VosObjectNode -> fold(element)
            is VosBracketBlockNode, is VosArrayNode -> fold(element)
            is VosClassBlockNode -> {
                val field = element.classFieldList.count()
                val placeholder = if (field > 1) "$field fields" else "$field field"
                fold(element, element.firstChild.textRange.endOffset, element.lastChild.textRange.startOffset, placeholder)
            }
            is VosUnionBlockNode -> {
                val variant = element.unionInnerList.count { it.unionField != null }
                val placeholder = if (variant > 1) "$variant variants" else "$variant variant"
                fold(element, element.firstChild.textRange.endOffset, element.lastChild.textRange.startOffset, placeholder)
            }
        }
        super.visitElement(element)
    }

    private fun fold(element: PsiElement, placeholder: String = "...", collapse: Boolean = false) {
        descriptors += FoldingDescriptor(element.node, element.textRange, null, setOf(), false, placeholder, collapse)
    }

    private fun fold(
        element: PsiElement,
        start: Int,
        end: Int,
        placeholder: String = "...",
        collapse: Boolean = false,
    ) {
        if (end < start) return
        descriptors += FoldingDescriptor(
            element.node,
            TextRange.create(start, end),
            null,
            setOf(),
            false,
            placeholder,
            collapse,
        )
    }
}
