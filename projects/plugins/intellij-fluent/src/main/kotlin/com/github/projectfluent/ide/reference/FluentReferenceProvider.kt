package com.github.projectfluent.ide.reference

import com.github.projectfluent.language.psi.FluentTypes
import com.github.projectfluent.language.psi.nodes.FluentAttributeIDNode
import com.github.projectfluent.language.psi.nodes.FluentMessageIDNode
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

class FluentReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        try {
            when (element) {
                is FluentMessageIDNode -> {
                    // 检查父元素是否为 SYMBOL_REFERENCE
                    val parent = element.parent
                    if (parent != null && parent.node != null && parent.node.elementType == FluentTypes.SYMBOL_REFERENCE) {
                        val symbolNode = element.node.findChildByType(FluentTypes.SYMBOL)
                        val symbol = symbolNode?.psi
                        if (symbol != null) {
                            return arrayOf(FluentMessageReference(symbol))
                        }
                    }
                }
                is FluentAttributeIDNode -> {
                    // 检查父元素是否为 SYMBOL_REFERENCE
                    val parent = element.parent
                    if (parent != null && parent.node != null && parent.node.elementType == FluentTypes.SYMBOL_REFERENCE) {
                        val symbolNode = element.node.findChildByType(FluentTypes.SYMBOL)
                        val symbol = symbolNode?.psi
                        if (symbol != null) {
                            return arrayOf(FluentAttributeReference(symbol))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // 忽略异常，返回空数组
        }
        return PsiReference.EMPTY_ARRAY
    }
}
