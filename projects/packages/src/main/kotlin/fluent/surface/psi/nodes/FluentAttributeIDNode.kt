package fluent.surface.psi.nodes

import fluent.surface.psi.FluentElement
import fluent.surface.psi.FluentVisitor
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor

class FluentAttributeIDNode(node: ASTNode) : FluentElement(node) {
    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is FluentVisitor) {
            visitor.visitAttributeID(this)
        } else {
            super.accept(visitor)
        }
    }
}
