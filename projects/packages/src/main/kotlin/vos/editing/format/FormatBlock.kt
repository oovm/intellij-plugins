package vos.editing.format

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.formatter.FormatterUtil
import vos.surface.psi.VosTypes
import vos.surface.psi.computeSpacing
import vos.surface.psi.isWhitespaceOrEmpty

class FormatBlock(
    private val node: ASTNode,
    private val alignment: Alignment?,
    private val indent: Indent?,
    private val wrap: Wrap?,
    private val ctx: FormatContext
) : ASTBlock {
    override fun isLeaf(): Boolean = node.firstChildNode == null

    override fun getNode() = node

    override fun getTextRange(): TextRange = node.textRange

    override fun getWrap() = wrap

    override fun getIndent() = indent

    override fun getAlignment() = alignment

    override fun getSpacing(child1: Block?, child2: Block) = computeSpacing(child1, child2, ctx)

    override fun getSubBlocks(): List<Block> = mySubBlocks

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val indent = when (node.elementType) {
            VosTypes.ARRAY -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
        return ChildAttributes(indent, null)
    }

    override fun isIncomplete(): Boolean = myIsIncomplete

    private val myIsIncomplete: Boolean by lazy {
        node.getChildren(null).any { it.elementType is PsiErrorElement } || FormatterUtil.isIncomplete(node)
    }

    private val mySubBlocks: List<Block> by lazy { buildChildren() }

    private fun buildChildren(): List<Block> {
        return node.getChildren(null)
            .filter { !it.isWhitespaceOrEmpty() }
            .map {
                FormatBlock(node = it, alignment = computeAlign(it), indent = computeIndent(it), wrap = null, ctx)
            }
    }

    private fun computeAlign(child: ASTNode): Alignment? {
        return when (child.elementType) {
            VosTypes.EQ -> {
                Alignment.createAlignment(true)
            }
            else -> null
        }
    }

    private fun computeIndent(child: ASTNode): Indent? {
        val isCornerChild = node.firstChildNode == child || node.lastChildNode == child
        return when (node.elementType) {
            VosTypes.BRACKET_BLOCK -> when {
                isCornerChild -> Indent.getNoneIndent()
                else -> Indent.getNormalIndent()
            }

            VosTypes.BRACE_BLOCK -> when {
                isCornerChild -> Indent.getNoneIndent()
                else -> Indent.getNormalIndent()
            }

            VosTypes.CLASS_BLOCK -> when {
                isCornerChild -> Indent.getNoneIndent()
                else -> Indent.getNormalIndent()
            }

            VosTypes.UNION_BLOCK -> when {
                isCornerChild -> Indent.getNoneIndent()
                else -> Indent.getNormalIndent()
            }

            else -> Indent.getNoneIndent()
        }
    }
}



