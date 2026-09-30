package jss.surface.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import jss.editing.structure.ViewElement
import jss.surface.ast.DeclareNode
import jss.surface.file.JssIcons
import javax.swing.Icon

class VosAnnotationNode(node: ASTNode) : VosNode(node)
class VosAnnotationBlockNode(node: ASTNode) : VosNode(node)
class VosAnnotationOneNode(node: ASTNode) : VosNode(node)

class JssArrayNode(node: ASTNode) : VosNode(node) {
    override fun getIcon(flags: Int): Icon = JssIcons.ANNOTATION
}

class VosBooleanNode(node: ASTNode) : VosNode(node)
class JssBraceBlockNode(node: ASTNode) : VosNode(node)
class JssBracketBlockNode(node: ASTNode) : VosNode(node)

class JssClassBlockNode(node: ASTNode) : VosNode(node) {
    val classFieldList: List<VosClassFieldNode>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, VosClassFieldNode::class.java)
}

class VosClassBoundNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosClassBoundNode = this
    override fun getIcon(flags: Int) = JssIcons.BOUND
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosClassFieldNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosClassFieldNode = this
    override fun getIcon(flags: Int) = JssIcons.FIELD
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosClassStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosClassStatementNode = this
    override fun getIcon(flags: Int) = JssIcons.CLASS
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
    val classBlock: JssClassBlockNode?
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
    override fun getIcon(flags: Int): Icon = JssIcons.ANNOTATION
    override fun getName(): String = when (firstChild?.elementType) {
        JssTypes.STRING -> text.substring(1, text.length - 1)
        else -> text
    }
}

class JssKvPairNode(node: ASTNode) : VosNode(node)

class VosLetStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosLetStatementNode = this
    override fun getIcon(flags: Int) = when (firstChild?.text) {
        "let", "val", "const" -> JssIcons.CONSTANT
        else -> JssIcons.MUTABLE
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
class JssObjectNode(node: ASTNode) : VosNode(node)
class VosSchemaNode(node: ASTNode) : VosNode(node)

class JssSchemaStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssSchemaStatementNode = this
    override fun getNameIdentifier(): PsiElement = TODO("Not yet implemented")
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")
    override fun getIcon(flags: Int): Icon = JssIcons.CLASS
}

class VosSetNode(node: ASTNode) : VosNode(node)
class VosStringLiteralNode(node: ASTNode) : VosNode(node)
class JssTypeExpressionNode(node: ASTNode) : VosNode(node)
class VosTypeGenericNode(node: ASTNode) : VosNode(node)
class VosTypeGenericBoundNode(node: ASTNode) : VosNode(node)
class VosTypeGenericCompareNode(node: ASTNode) : VosNode(node)
class VosTypeGenericRangeNode(node: ASTNode) : VosNode(node)
class VosTypeNumberNode(node: ASTNode) : VosNode(node)
class JssTypeSymbolNode(node: ASTNode) : VosNode(node)

class JssUnionBlockNode(node: ASTNode) : VosNode(node) {
    val unionInnerList: List<VosUnionInnerNode>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, VosUnionInnerNode::class.java)
}

class VosUnionFieldNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): VosUnionFieldNode = this
    override fun getIcon(flags: Int) = JssIcons.FIELD
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
    override fun getIcon(flags: Int): Icon = JssIcons.UNION
    override fun getNameIdentifier(): VosIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: VosIdentifierNode
        get() = childRequired()
}

class VosUrlMaybeValidNode(node: ASTNode) : VosNode(node)

class VosValueNode(node: ASTNode) : VosNode(node) {
    override fun getOriginalElement(): VosValueNode = this
    override fun getIcon(flags: Int): Icon = JssIcons.ANNOTATION
}

private inline fun <reified T : VosNode> VosNode.child(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

private inline fun <reified T : VosNode> DeclareNode.child(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

private inline fun <reified T : VosNode> DeclareNode.childRequired(): T =
    child() ?: error("Missing ${T::class.java.simpleName} in $this")
