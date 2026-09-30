package yggdrasil.surface.ast


import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoType
import com.intellij.codeInsight.daemon.impl.analysis.HighlightInfoHolder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import yggdrasil.editing.highlight.HighlightColor


fun ASTNode?.isWhitespaceOrEmpty(): Boolean {
    return this == null || textLength == 0 || elementType == TokenType.WHITE_SPACE
}

fun HighlightInfoHolder.register(element: PsiElement?, color: HighlightColor) {
    if (element == null) return
    val builder = HighlightInfo.newHighlightInfo(HighlightInfoType.INFORMATION)
    builder.textAttributes(color.textAttributesKey)
    builder.range(element.textRange)
    this.add(builder.create())
}