package com.github.projectfluent.ide.reference

import com.github.projectfluent.language.psi.FluentTypes
import com.intellij.psi.*
import com.intellij.psi.tree.IElementType

class FluentAttributeReference(element: PsiElement) : PsiReferenceBase<PsiElement>(element) {
    override fun resolve(): PsiElement? {
        val attributeName = element.text
        val messageReference = element.parent.parent // MESSAGE_REFERENCE
        val messageIdNode = messageReference.node.findChildByType(FluentTypes.MESSAGE_ID)
        val messageId = messageIdNode?.psi
        val symbolNode = messageIdNode?.findChildByType(FluentTypes.SYMBOL)
        val messageName = symbolNode?.psi?.text
        
        if (messageName == null) return null
        
        val file = element.containingFile
        
        // 遍历文件中的所有消息定义，查找匹配的消息
        val visitor = object : PsiElementVisitor() {
            var result: PsiElement? = null
            
            override fun visitElement(element: PsiElement) {
                if (result != null) return
                
                if (element.node.elementType == FluentTypes.MESSAGE) {
                    val msgIdNode = element.node.findChildByType(FluentTypes.MESSAGE_ID)
                    if (msgIdNode != null) {
                        val msgSymbolNode = msgIdNode.findChildByType(FluentTypes.SYMBOL)
                        val msgSymbol = msgSymbolNode?.psi
                        if (msgSymbol != null && msgSymbol.text == messageName) {
                            // 查找该消息的属性
                            val attributes = element.getChildrenOfType(FluentTypes.ATTRIBUTE)
                            for (attribute in attributes) {
                                val attrIdNode = attribute.node.findChildByType(FluentTypes.ATTRIBUTE_ID)
                                if (attrIdNode != null) {
                                    val attrSymbolNode = attrIdNode.findChildByType(FluentTypes.SYMBOL)
                                    val attrSymbol = attrSymbolNode?.psi
                                    if (attrSymbol != null && attrSymbol.text == attributeName) {
                                        result = attrSymbol
                                        return
                                    }
                                }
                            }
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
        // 获取消息名称
        val messageReference = element.parent.parent // MESSAGE_REFERENCE
        val messageIdNode = messageReference.node.findChildByType(FluentTypes.MESSAGE_ID)
        val symbolNode = messageIdNode?.findChildByType(FluentTypes.SYMBOL)
        val messageName = symbolNode?.psi?.text
        
        if (messageName == null) return emptyArray()
        
        val file = element.containingFile
        val variants = mutableListOf<Any>()
        
        // 查找消息并收集其属性
        val visitor = object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element.node.elementType == FluentTypes.MESSAGE) {
                    val msgIdNode = element.node.findChildByType(FluentTypes.MESSAGE_ID)
                    if (msgIdNode != null) {
                        val msgSymbolNode = msgIdNode.findChildByType(FluentTypes.SYMBOL)
                        val msgSymbol = msgSymbolNode?.psi
                        if (msgSymbol != null && msgSymbol.text == messageName) {
                            // 收集该消息的所有属性
                            val attributes = element.getChildrenOfType(FluentTypes.ATTRIBUTE)
                            for (attribute in attributes) {
                                val attrIdNode = attribute.node.findChildByType(FluentTypes.ATTRIBUTE_ID)
                                if (attrIdNode != null) {
                                    val attrSymbolNode = attrIdNode.findChildByType(FluentTypes.SYMBOL)
                                    val attrSymbol = attrSymbolNode?.psi
                                    if (attrSymbol != null) {
                                        variants.add(attrSymbol)
                                    }
                                }
                            }
                        }
                    }
                }
                
                element.acceptChildren(this)
            }
        }
        
        file.accept(visitor)
        return variants.toTypedArray()
    }
    
    private fun PsiElement.getChildrenOfType(type: IElementType): List<PsiElement> {
        val result = mutableListOf<PsiElement>()
        for (child in this.children) {
            if (child.node.elementType == type) {
                result.add(child)
            }
        }
        return result
    }
}
