package vos.surface.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import vos.editing.structure.ViewElement
import vos.surface.ast.DeclareNode
import vos.surface.file.VosIcons
import javax.swing.Icon

class VosAnnotationNode(node: ASTNode) : VosNode(node)
class VosAnnotationBlockNode(node: ASTNode) : VosNode(node)
class VosAnnotationOneNode(node: ASTNode) : VosNode(node)

class VosArrayNode(node: ASTNode) : VosNode(node) {
    override fun getIcon(flags: Int): Icon = VosIcons.ANNOTATION
}

class VosBooleanNode(node: ASTNode) : VosNode(node)
class VosBraceBlockNode(node: ASTNode) : VosNode(node)
class VosBracketBlockNode(node: ASTNode) : VosNode(node)

class VosClassBlockNode(node: ASTNode) : VosNode(node) {
    val classFieldList: List<VosClassFieldNode>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, VosClassFieldNode::class.java)
}

class VosClassBoundNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosClassBoundNode = this
    override fun getIcon(flags: Int) = VosIcons.BOUND
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosClassFieldNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosClassFieldNode = this
    override fun getIcon(flags: Int) = VosIcons.FIELD
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosClassStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosClassStatementNode = this
    override fun getIcon(flags: Int) = VosIcons.CLASS
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
    val classBlock: VosClassBlockNode?
        get() = child()

    override fun getChildrenView(): Array<ViewElement> {
        val block = classBlock ?: return emptyArray()
        val out = mutableListOf<ViewElement>()
        for (it in block.searchChildrenOfType(VosClassBoundNode::class.java)) {
            out.add(ViewElement(it))
        }
        for (it in block.searchChildrenOfType(VosClassFieldNode::class.java)) {
            out.add(ViewElement(it))
        }
        return out.toTypedArray()
    }
}

class VosCompareNode(node: ASTNode) : VosNode(node)
class VosIdentifierNode(node: ASTNode) : VosNode(node)
class VosIntegerSignedNode(node: ASTNode) : VosNode(node)

class VosKeyNode(node: ASTNode) : VosNode(node) {
    override fun getOriginalElement(): VosKeyNode = this
    override fun getIcon(flags: Int): Icon = VosIcons.ANNOTATION
    override fun getName(): String = when (firstChild?.elementType) {
        VosTypes.STRING -> text.substring(1, text.length - 1)
        else -> text
    }
}

class VosKvPairNode(node: ASTNode) : VosNode(node)

class VosLetStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosLetStatementNode = this
    override fun getIcon(flags: Int) = when (firstChild?.text) {
        "let", "val", "const" -> VosIcons.CONSTANT
        else -> VosIcons.MUTABLE
    }
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosModifiersNode(node: ASTNode) : VosNode(node)
class VosNamespaceNode(node: ASTNode) : VosNode(node)
class VosNamespaceStatementNode(node: ASTNode) : VosNode(node)
class VosNullNode(node: ASTNode) : VosNode(node)
class VosObjectNode(node: ASTNode) : VosNode(node)
class VosSchemaNode(node: ASTNode) : VosNode(node)

class VosSchemaStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosSchemaStatementNode = this
    override fun getNameIdentifier(): PsiElement = TODO("Not yet implemented")
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")
    override fun getIcon(flags: Int): Icon = VosIcons.CLASS
}

class VosSetNode(node: ASTNode) : VosNode(node)
class VosStringLiteralNode(node: ASTNode) : VosNode(node)
class VosTypeExpressionNode(node: ASTNode) : VosNode(node)
class VosTypeGenericNode(node: ASTNode) : VosNode(node)
class VosTypeGenericBoundNode(node: ASTNode) : VosNode(node)
class VosTypeGenericCompareNode(node: ASTNode) : VosNode(node)
class VosTypeGenericRangeNode(node: ASTNode) : VosNode(node)
class VosTypeNumberNode(node: ASTNode) : VosNode(node)
class VosTypeSymbolNode(node: ASTNode) : VosNode(node)

class VosUnionBlockNode(node: ASTNode) : VosNode(node) {
    val unionInnerList: List<VosUnionInnerNode>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, VosUnionInnerNode::class.java)
}

class VosUnionFieldNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosUnionFieldNode = this
    override fun getIcon(flags: Int) = VosIcons.FIELD
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosUnionInnerNode(node: ASTNode) : VosNode(node) {
    val unionField: VosUnionFieldNode?
        get() = PsiTreeUtil.getChildOfType(this, VosUnionFieldNode::class.java)
}

class VosUnionStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosUnionStatementNode = this
    override fun getIcon(flags: Int): Icon = VosIcons.UNION
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosUrlMaybeValidNode(node: ASTNode) : VosNode(node)

class VosValueNode(node: ASTNode) : VosNode(node) {
    override fun getOriginalElement(): VosValueNode = this
    override fun getIcon(flags: Int): Icon = VosIcons.ANNOTATION
}

private inline fun <reified T : VosNode> VosNode.child(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

private inline fun <reified T : VosNode> DeclareNode.child(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

private inline fun <reified T : VosNode> DeclareNode.childRequired(): T =
    child() ?: error("Missing ${T::class.java.simpleName} in $this")
