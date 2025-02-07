package com.github.projectfluent.ide.reference

import com.github.projectfluent.language.psi.FluentTypes
import com.intellij.psi.*

class FluentMessageReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {
    override fun resolve(): PsiElement? {
        val messageName = element.text
        val file = element.containingFile
        
        // 遍历文件中的所有消息定义，查找匹配的消息ID
        val visitor = object : PsiElementVisitor() {
            var result: PsiElement? = null
            
            override fun visitElement(element: PsiElement) {
                if (result != null) return
                
                if (element.node.elementType == FluentTypes.MESSAGE) {
                    val messageIdNode = element.node.findChildByType(FluentTypes.MESSAGE_ID)
                    if (messageIdNode != null) {
                        val symbolNode = messageIdNode.findChildByType(FluentTypes.SYMBOL)
                        val symbol = symbolNode?.psi
                        if (symbol != null && symbol.text == messageName) {
                            result = symbol
                            return
                        }
                    }
                }
                
                element.acceptChildren(this)
            }
        }
        
        file.accept(visitor)
        return visitor.result
    }
    
    override fun getVariants(): Array<Any> {
        val file = element.containingFile
        val variants = mutableListOf<Any>()
        
        // 收集文件中的所有消息定义，用于代码补全
        val visitor = object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType == FluentTypes.MESSAGE) {
                    val messageIdNode = element.node.findChildByType(FluentTypes.MESSAGE_ID)
                    if (messageIdNode != null) {
                        val symbolNode = messageIdNode.findChildByType(FluentTypes.SYMBOL)
                        val symbol = symbolNode?.psi
                        if (symbol != null) {
                            variants.add(symbol)
                        }
                    }
                }
                
                element.acceptChildren(this)
            }
        }
        
        file.accept(visitor)
        return variants.toTypedArray()
    }
}
