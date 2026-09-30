package fluent.editing.format

import fluent.surface.ast.computeSpacing
import fluent.surface.ast.isWhitespaceOrEmpty
import fluent.surface.psi.FluentTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.formatter.FormatterUtil

class FluentFormatBlock(
    internal val node: ASTNode,
    private val alignment: Alignment?,
    private val indent: Indent?,
    private val wrap: Wrap?,
    private val space: FluentFormatSpace,
) : ASTBlock {
    private val myIsIncomplete: Boolean by lazy {
        node.getChildren(null).any { it.elementType is PsiErrorElement } || FormatterUtil.isIncomplete(node)
    }

    private val visibleChildren: List<ASTNode> by lazy {
        node.getChildren(null).filterNot(::shouldSkipChild)
    }

    private val mySubBlocks: List<Block> by lazy { buildChildren() }

    private fun buildChildren(): List<Block> {
        return visibleChildren
            .map { childNode ->
                FluentFormatBlock(
                    node = childNode,
                    alignment = null,
                    indent = computeIndent(childNode),
                    wrap = null,
                    space
                )
            }
    }

    override fun isLeaf(): Boolean = node.firstChildNode == null

    override fun getNode() = node

    override fun getTextRange(): TextRange = node.textRange

    override fun getWrap() = wrap

    override fun getIndent() = indent

    override fun getAlignment() = alignment

    override fun getSpacing(child1: Block?, child2: Block) = computeSpacing(child1, child2, space)

    override fun getSubBlocks(): List<Block> = mySubBlocks

    override fun isIncomplete(): Boolean = myIsIncomplete

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val indent = when (node.elementType) {
            FluentTypes.MESSAGE,
            FluentTypes.TERM,
            FluentTypes.ATTRIBUTE,
            FluentTypes.PATTERN,
            FluentTypes.SELECT_EXPRESSION,
            FluentTypes.VARIANT,
            FluentTypes.DEFAULT_VARIANT -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
        return ChildAttributes(indent, null)
    }

    private fun computeIndent(child: ASTNode): Indent? {
        return when (node.elementType) {
            FluentTypes.MESSAGE, FluentTypes.TERM, FluentTypes.ATTRIBUTE -> when {
                // Only the value / nested attributes take a continuation indent (#5).
                child.elementType == FluentTypes.PATTERN ||
                    child.elementType == FluentTypes.ATTRIBUTE -> Indent.getNormalIndent()
                else -> Indent.getNoneIndent()
            }

            // Indent select arms once; selector / braces stay flat (#5).
            // DEFAULT_VARIANT is one space shallower so `*[other]` aligns `[` with siblings.
            FluentTypes.SELECT_EXPRESSION -> when (child.elementType) {
                FluentTypes.VARIANT -> Indent.getNormalIndent()
                FluentTypes.DEFAULT_VARIANT -> {
                    val indentSize = space.commonSettings.indentOptions?.INDENT_SIZE ?: 2
                    Indent.getSpaceIndent((indentSize - 1).coerceAtLeast(0))
                }
                else -> Indent.getNoneIndent()
            }

            FluentTypes.VARIANT,
            FluentTypes.DEFAULT_VARIANT,
            FluentTypes.PATTERN,
            FluentTypes.BLOCK_PLACEABLE,
            FluentTypes.CALL_ARGUMENTS -> Indent.getNoneIndent()
            else -> Indent.getNoneIndent()
        }
    }

    private fun shouldSkipChild(child: ASTNode): Boolean {
        if (child.isWhitespaceOrEmpty()) {
            return true
        }

        if (child.elementType != FluentTypes.INLINE_BLANK) {
            return false
        }

        return when (node.elementType) {
            // Keep blanks in textual nodes; dropping them merges words or splits variant arms (#5).
            FluentTypes.PATTERN,
            FluentTypes.SELECT_EXPRESSION,
            FluentTypes.INLINE_PLACEABLE,
            FluentTypes.VARIANT,
            FluentTypes.DEFAULT_VARIANT -> false
            else -> true
        }
    }
}
