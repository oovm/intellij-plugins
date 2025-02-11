package com.github.projectfluent.language.ast

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
        FluentTypes.INLINE_BLANK,
        FluentTypes.INDENT
    )
}

fun Block.computeSpacing(child1: Block?, child2: Block, ctx: FluentFormatSpace): Spacing? {
    return ctx.spacingBuilder.getSpacing(this, child1, child2)
}


