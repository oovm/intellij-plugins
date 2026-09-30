package fluent.editing.assist

import fluent.surface.psi.FluentRecursiveVisitor
import fluent.surface.psi.nodes.FluentAttributeNode
import fluent.surface.psi.nodes.FluentInlinePlaceableNode
import fluent.surface.psi.nodes.FluentMessageNode
import fluent.surface.psi.nodes.FluentTermNode
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

class FluentFoldingVisitor(private val descriptors: MutableList<FoldingDescriptor>) : FluentRecursiveVisitor() {

    override fun visitInlinePlaceable(inlinePlaceable: FluentInlinePlaceableNode) {
        val firstChild = inlinePlaceable.firstChild
        val lastChild = inlinePlaceable.lastChild
        if (firstChild != null && lastChild != null && firstChild != lastChild) {
            val startOffset = firstChild.textRange.endOffset
            val endOffset = lastChild.textRange.endOffset
            if (startOffset < endOffset) {
                descriptors += FoldingDescriptor(
                    inlinePlaceable,
                    TextRange(startOffset, endOffset)
                )
            }
        }
        super.visitInlinePlaceable(inlinePlaceable)
    }

    private fun addMessageOrTermFolding(element: PsiElement) {
        val children = element.children
        if (children.isEmpty()) return
        
        val firstChild = children.first()
        val lastChild = children.lastOrNull { it.textLength > 0 } ?: return
        
        if (firstChild == lastChild) return
        
        val startOffset = firstChild.textRange.endOffset
        val endOffset = lastChild.textRange.endOffset
        
        if (startOffset < endOffset) {
            descriptors += FoldingDescriptor(
                element,
                TextRange(startOffset, endOffset)
            )
        }
    }

    private fun addAttributeFolding(attribute: FluentAttributeNode) {
        val children = attribute.children
        if (children.size < 2) return
        
        val firstChild = children.first()
        val lastChild = children.lastOrNull { it.textLength > 0 } ?: return
        
        if (firstChild == lastChild) return
        
        val startOffset = firstChild.textRange.endOffset
        val endOffset = lastChild.textRange.endOffset
        
        if (startOffset < endOffset) {
            descriptors += FoldingDescriptor(
                attribute,
                TextRange(startOffset, endOffset)
            )
        }
    }

    override fun visitMessage(message: FluentMessageNode) {
        addMessageOrTermFolding(message)
        super.visitMessage(message)
    }

    override fun visitAttribute(attribute: FluentAttributeNode) {
        addAttributeFolding(attribute)
        super.visitAttribute(attribute)
    }

    override fun visitTerm(term: FluentTermNode) {
        addMessageOrTermFolding(term)
        super.visitTerm(term)
    }
}
