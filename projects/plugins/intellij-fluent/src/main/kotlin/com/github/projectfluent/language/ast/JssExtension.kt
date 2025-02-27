package com.github.projectfluent.language.ast

import com.github.projectfluent.ide.formatter.FluentFormatBlock
import com.github.projectfluent.ide.formatter.FluentFormatSpace
import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.github.projectfluent.language.psi.FluentTypes
import com.intellij.psi.TokenType


fun ASTNode?.isWhitespaceOrEmpty(): Boolean {
    return this == null || textLength == 0 || elementType in setOf(
        TokenType.WHITE_SPACE,
        FluentTypes.LINE_END,
        FluentTypes.INDENT
    )
}

fun Block.computeSpacing(child1: Block?, child2: Block, ctx: FluentFormatSpace): Spacing? {
    val explicit = ctx.spacingBuilder.getSpacing(this, child1, child2)
    if (explicit != null) {
        return explicit
    }

    val parentType = (this as? FluentFormatBlock)?.node?.elementType
    if (parentType == FluentTypes.SELECT_EXPRESSION) {
        // Do not preserve source line breaks between select arms (#5).
        return Spacing.createSpacing(0, 0, 0, false, 0)
    }
    return null
}


