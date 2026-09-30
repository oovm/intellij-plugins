package jss.editing.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import jss.surface.psi.JssBraceBlockNode
import jss.surface.psi.JssKvPairNode
import jss.surface.psi.JssSchemaStatementNode
import jss.surface.psi.JssTypeExpressionNode
import jss.surface.psi.JssTypeSymbolNode

internal enum class SchemaCollectionKind {
    OBJECT,
    ARRAY,
    UNKNOWN,
}

internal object SchemaCompletionContext {
    fun collectionKind(position: PsiElement): SchemaCollectionKind =
        when (declaredType(position)?.lowercase()) {
            "object" -> SchemaCollectionKind.OBJECT
            "array" -> SchemaCollectionKind.ARRAY
            else -> SchemaCollectionKind.UNKNOWN
        }

    fun existingPropertyKeys(position: PsiElement): Set<String> {
        val block = PsiTreeUtil.getParentOfType(position, JssBraceBlockNode::class.java, false) ?: return emptySet()
        return PsiTreeUtil.getChildrenOfType(block, JssKvPairNode::class.java)
            ?.mapNotNull { pair -> pair.firstChild?.text?.trim('"') }
            ?.toSet()
            ?: emptySet()
    }

    private fun declaredType(position: PsiElement): String? {
        val block = PsiTreeUtil.getParentOfType(position, JssBraceBlockNode::class.java, false) ?: return null
        val statement = block.parent as? JssSchemaStatementNode ?: return null
        val typeExpression = PsiTreeUtil.getChildOfType(statement, JssTypeExpressionNode::class.java) ?: return null
        return PsiTreeUtil.getChildOfType(typeExpression, JssTypeSymbolNode::class.java)?.text
    }
}
