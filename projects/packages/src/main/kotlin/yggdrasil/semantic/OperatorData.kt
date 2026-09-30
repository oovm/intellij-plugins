package yggdrasil.semantic.symbol

import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import yggdrasil.editing.documentation.DocumentationRenderer
import yggdrasil.editing.highlight.HighlightColor

//import nexus.language.psi.ValkyrieTypes

@Suppress("MemberVisibilityCanBePrivate")
class OperatorData(
    val name: String,
    val symbol: String,
    val detail: String = "",
    val associative: OperatorAssociativity = OperatorAssociativity.NONE,
    val priority: Int = 100,
) {
    fun documentation(doc: DocumentationRenderer) {
        doc.append(HighlightColor.KEYWORD, "operator ")
        doc.append(HighlightColor.SYM_MACRO, symbol)
        doc.append(HighlightColor.KEYWORD, " de-sugars ")
        doc.append(HighlightColor.SYM_FUNCTION, name)
        doc.append("<br/>")
        doc.append(HighlightColor.KEYWORD, "associative ")
        doc.append(HighlightColor.SYM_MACRO, associative.toString())
        doc.append("<br/>")
        doc.append(HighlightColor.KEYWORD, "priority ")
        doc.append(HighlightColor.SYM_CONSTANT, priority.toString())
        doc.append("<hr/>")
        doc.append(detail)
    }

    companion object {
        fun builtinData(name: PsiElement): OperatorData? = when (name.elementType) {
//            ValkyrieTypes.OP_ADD -> OperatorData(
//                "add",
//                "+",
//                """
//                a + b
//                """.trimIndent()
//            )
//            ValkyrieTypes.OP_INC -> OperatorData(
//                "++",
//                "increase",
//                """
//                a ++ b
//                ++a
//                """.trimIndent()
//            )
            else -> null
        }
    }
}