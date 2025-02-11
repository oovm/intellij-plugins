package com.github.projectfluent.ide.formatter

import com.github.projectfluent.language.ast.computeSpacing
import com.github.projectfluent.language.ast.isWhitespaceOrEmpty
import com.github.projectfluent.language.psi.FluentTypes
import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.formatter.FormatterUtil

class FluentFormatBlock(
    private val node: ASTNode,
    private val alignment: Alignment?,
    private val indent: Indent?,
    private val wrap: Wrap?,
    private val space: FluentFormatSpace,
) : ASTBlock {
    private val myIsIncomplete: Boolean by lazy {
        node.getChildren(null).any { it.elementType is PsiErrorElement } || FormatterUtil.isIncomplete(node)
    }

    private val visibleChildren: List<ASTNode> by lazy {
        node.getChildren(null).filter { !it.isWhitespaceOrEmpty() }
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
            FluentTypes.VARIANT -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
        return ChildAttributes(indent, null)
    }

    private fun computeIndent(child: ASTNode): Indent? {
        val firstVisibleChild = visibleChildren.firstOrNull()
        val firstLine = firstVisibleChild == child
        return when (node.elementType) {
            FluentTypes.MESSAGE, FluentTypes.TERM, FluentTypes.ATTRIBUTE -> when {
                firstLine -> Indent.getNoneIndent()
                else -> Indent.getNormalIndent()
            }

            // Only line-structure boundaries should add indentation.
            // Wrapper nodes like PATTERN/BLOCK_PLACEABLE/CALL_ARGUMENTS would
            // otherwise stack indentation on every AST level and produce
            // wildly over-indented Fluent continuations.
            FluentTypes.SELECT_EXPRESSION -> when (child.elementType) {
                FluentTypes.VARIANT -> Indent.getNormalIndent()
                else -> Indent.getNoneIndent()
            }

            FluentTypes.VARIANT,
            FluentTypes.PATTERN,
            FluentTypes.BLOCK_PLACEABLE,
            FluentTypes.CALL_ARGUMENTS -> Indent.getNoneIndent()
            else -> Indent.getNoneIndent()
        }
    }
}
