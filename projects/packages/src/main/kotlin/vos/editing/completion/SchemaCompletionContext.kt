package vos.editing.completion

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import vos.surface.psi.VosBraceBlockNode
import vos.surface.psi.VosKvPairNode
import vos.surface.psi.VosSchemaStatementNode
import vos.surface.psi.VosTypeExpressionNode
import vos.surface.psi.VosTypeSymbolNode

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
        val block = PsiTreeUtil.getParentOfType(position, VosBraceBlockNode::class.java, false) ?: return emptySet()
        return PsiTreeUtil.getChildrenOfType(block, VosKvPairNode::class.java)
            ?.mapNotNull { pair -> pair.firstChild?.text?.trim('"') }
            ?.toSet()
            ?: emptySet()
    }

    private fun declaredType(position: PsiElement): String? {
        val block = PsiTreeUtil.getParentOfType(position, VosBraceBlockNode::class.java, false) ?: return null
        val statement = block.parent as? VosSchemaStatementNode ?: return null
        val typeExpression = PsiTreeUtil.getChildOfType(statement, VosTypeExpressionNode::class.java) ?: return null
        return PsiTreeUtil.getChildOfType(typeExpression, VosTypeSymbolNode::class.java)?.text
    }
}
