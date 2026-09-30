package jss.surface.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import jss.editing.structure.ViewElement
import jss.surface.ast.DeclareNode
import jss.surface.file.JssIcons
import javax.swing.Icon

class JssAnnotationNode(node: ASTNode) : JssNode(node)
class JssAnnotationBlockNode(node: ASTNode) : JssNode(node)
class JssAnnotationOneNode(node: ASTNode) : JssNode(node)

class JssArrayNode(node: ASTNode) : JssNode(node) {
    override fun getIcon(flags: Int): Icon = JssIcons.ANNOTATION
}

class JssBooleanNode(node: ASTNode) : JssNode(node)
class JssBraceBlockNode(node: ASTNode) : JssNode(node)
class JssBracketBlockNode(node: ASTNode) : JssNode(node)

class JssClassBlockNode(node: ASTNode) : JssNode(node) {
    val classFieldList: List<JssClassFieldNode>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, JssClassFieldNode::class.java)
}

class JssClassBoundNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssClassBoundNode = this
    override fun getIcon(flags: Int) = JssIcons.BOUND
    override fun getNameIdentifier(): JssIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: JssIdentifierNode
        get() = childRequired()
}

class JssClassFieldNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssClassFieldNode = this
    override fun getIcon(flags: Int) = JssIcons.FIELD
    override fun getNameIdentifier(): JssIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: JssIdentifierNode
        get() = childRequired()
}

class JssClassStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssClassStatementNode = this
    override fun getIcon(flags: Int) = JssIcons.CLASS
    override fun getNameIdentifier(): JssIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: JssIdentifierNode
        get() = childRequired()
    val classBlock: JssClassBlockNode?
        get() = child()

    override fun getChildrenView(): Array<ViewElement> {
        val block = classBlock ?: return emptyArray()
        val out = mutableListOf<ViewElement>()
        for (it in block.searchChildrenOfType(JssClassBoundNode::class.java)) {
            out.add(ViewElement(it))
        }
        for (it in block.searchChildrenOfType(JssClassFieldNode::class.java)) {
            out.add(ViewElement(it))
        }
        return out.toTypedArray()
    }
}

class JssCompareNode(node: ASTNode) : JssNode(node)
class JssIdentifierNode(node: ASTNode) : JssNode(node)
class JssIntegerSignedNode(node: ASTNode) : JssNode(node)

class JssKeyNode(node: ASTNode) : JssNode(node) {
    override fun getOriginalElement(): JssKeyNode = this
    override fun getIcon(flags: Int): Icon = JssIcons.ANNOTATION
    override fun getName(): String = when (firstChild?.elementType) {
        JssTypes.STRING -> text.substring(1, text.length - 1)
        else -> text
    }
}

class JssKvPairNode(node: ASTNode) : JssNode(node)

class JssLetStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssLetStatementNode = this
    override fun getIcon(flags: Int) = when (firstChild?.text) {
        "let", "val", "const" -> JssIcons.CONSTANT
        else -> JssIcons.MUTABLE
    }
    override fun getNameIdentifier(): JssIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: JssIdentifierNode
        get() = childRequired()
}

class JssModifiersNode(node: ASTNode) : JssNode(node)
class JssNamespaceNode(node: ASTNode) : JssNode(node)
class JssNamespaceStatementNode(node: ASTNode) : JssNode(node)
class JssNullNode(node: ASTNode) : JssNode(node)
class JssObjectNode(node: ASTNode) : JssNode(node)
class JssSchemaNode(node: ASTNode) : JssNode(node)

class JssSchemaStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssSchemaStatementNode = this
    override fun getNameIdentifier(): PsiElement = TODO("Not yet implemented")
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")
    override fun getIcon(flags: Int): Icon = JssIcons.CLASS
}

class JssSetNode(node: ASTNode) : JssNode(node)
class JssStringLiteralNode(node: ASTNode) : JssNode(node)
class JssTypeExpressionNode(node: ASTNode) : JssNode(node)
class JssTypeGenericNode(node: ASTNode) : JssNode(node)
class JssTypeGenericBoundNode(node: ASTNode) : JssNode(node)
class JssTypeGenericCompareNode(node: ASTNode) : JssNode(node)
class JssTypeGenericRangeNode(node: ASTNode) : JssNode(node)
class JssTypeNumberNode(node: ASTNode) : JssNode(node)
class JssTypeSymbolNode(node: ASTNode) : JssNode(node)

class JssUnionBlockNode(node: ASTNode) : JssNode(node) {
    val unionInnerList: List<JssUnionInnerNode>
        get() = PsiTreeUtil.getChildrenOfTypeAsList(this, JssUnionInnerNode::class.java)
}

class JssUnionFieldNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssUnionFieldNode = this
    override fun getIcon(flags: Int) = JssIcons.FIELD
    override fun getNameIdentifier(): JssIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: JssIdentifierNode
        get() = childRequired()
}

class JssUnionInnerNode(node: ASTNode) : JssNode(node) {
    val unionField: JssUnionFieldNode?
        get() = PsiTreeUtil.getChildOfType(this, JssUnionFieldNode::class.java)
}

class JssUnionStatementNode(node: ASTNode) : DeclareNode(node) {
    override fun getOriginalElement(): JssUnionStatementNode = this
    override fun getIcon(flags: Int): Icon = JssIcons.UNION
    override fun getNameIdentifier(): JssIdentifierNode = identifier
    override fun setName(name: String): PsiElement = TODO("Not yet implemented")

    val identifier: JssIdentifierNode
        get() = childRequired()
}

class JssUrlMaybeValidNode(node: ASTNode) : JssNode(node)

class JssValueNode(node: ASTNode) : JssNode(node) {
    override fun getOriginalElement(): JssValueNode = this
    override fun getIcon(flags: Int): Icon = JssIcons.ANNOTATION
}

private inline fun <reified T : JssNode> JssNode.child(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

private inline fun <reified T : JssNode> DeclareNode.child(): T? =
    PsiTreeUtil.getChildOfType(this, T::class.java)

private inline fun <reified T : JssNode> DeclareNode.childRequired(): T =
    child() ?: error("Missing ${T::class.java.simpleName} in $this")
