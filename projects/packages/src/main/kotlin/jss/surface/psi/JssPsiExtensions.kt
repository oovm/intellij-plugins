package jss.surface.psi

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import jss.editing.format.FormatContext


fun <T : PsiElement?> PsiElement?.searchChildrenOfType(vararg elements: Class<out T>, recursive: Boolean = false): Collection<T> {
    if (this == null) return emptyList()
    return when {
        recursive -> PsiTreeUtil.findChildrenOfAnyType(this, *elements)
        else -> PsiTreeUtil.getChildrenOfAnyType(this, *elements)
    }
}

fun ASTNode?.isWhitespaceOrEmpty(): Boolean {
    return this == null || textLength == 0 || elementType == TokenType.WHITE_SPACE
}

fun Block.computeSpacing(child1: Block?, child2: Block, ctx: FormatContext): Spacing? {
    return ctx.spacingBuilder.getSpacing(this, child1, child2)
}
