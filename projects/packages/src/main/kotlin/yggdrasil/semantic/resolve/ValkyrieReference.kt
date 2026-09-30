package yggdrasil.semantic.resolve.declaration

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import yggdrasil.editing.highlight.HighlightColor
import yggdrasil.editing.highlight.YggdrasilSemanticHighlighter
import yggdrasil.surface.psi.nodes.YggdrasilDefineClass
import yggdrasil.surface.psi.nodes.YggdrasilDefineUnion
import yggdrasil.surface.psi.nodes.YggdrasilGroupItemNode
import yggdrasil.surface.psi.nodes.YggdrasilIdentifierNode

open class ValkyrieReference : PsiPolyVariantReference {
    private val _element: YggdrasilIdentifierNode

    constructor(element: YggdrasilIdentifierNode) {
        this._element = element
    }

    override fun getElement(): YggdrasilIdentifierNode {
        return _element
    }

    override fun getRangeInElement(): TextRange {
        return TextRange(0, _element.text.length)
    }

    override fun resolve(): PsiElement? {
        return null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        return resolveSequence()
            .map {
                PsiElementResolveResult(it)
            }
            .toList().toTypedArray()
    }

  private  fun resolveSequence(): Sequence<PsiNameIdentifierOwner> {
      // TODO: Implement proper resolution
      return emptySequence()
    }


    override fun getCanonicalText(): String {
        TODO("Not yet implemented")
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        TODO("Not yet implemented")
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        TODO("Not yet implemented")
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        return resolve() == element
    }

    override fun isSoft(): Boolean {
        TODO("Not yet implemented")
    }


    fun highlight(highlighter: YggdrasilSemanticHighlighter) {
        return when (resolveSequence().firstOrNull()) {
            is YggdrasilDefineClass -> highlighter.highlight(_element, HighlightColor.RULE_CLASS)
            is YggdrasilDefineUnion -> highlighter.highlight(_element, HighlightColor.RULE_UNION)
            is YggdrasilGroupItemNode -> highlighter.highlight(_element, HighlightColor.SYM_CONSTANT)
            else -> {

            }
        }
    }
}