package yggdrasil.semantic.resolve.declaration


//import nexus.language.psi_node.ValkyrieClassStatementNode
import com.intellij.codeInsight.hint.DeclarationRangeHandler
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.endOffset
import com.intellij.psi.util.startOffset
import yggdrasil.surface.psi.nodes.YggdrasilDefineClass


class ClassDeclarationHandler : DeclarationRangeHandler<YggdrasilDefineClass> {
    override fun getDeclarationRange(container: YggdrasilDefineClass): TextRange {
        val startOffset = container.startOffset
        val endOffset = container.endOffset
        return TextRange(startOffset, endOffset)
    }
}


