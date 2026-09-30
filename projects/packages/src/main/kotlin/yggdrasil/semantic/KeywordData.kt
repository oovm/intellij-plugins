@file:Suppress("ConvertSecondaryConstructorToPrimary", "MemberVisibilityCanBePrivate", "UnstableApiUsage")

package yggdrasil.semantic.symbol

import com.intellij.model.Pointer
import com.intellij.model.Symbol
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import yggdrasil.editing.documentation.DocumentationRenderer

class KeywordData : Symbol, Pointer<KeywordData> {
    val name: String
    val detail: String

    constructor(name: String, detail: String = "") {
        this.name = name
        this.detail = detail
    }

    override fun createPointer(): Pointer<out KeywordData> = this
    override fun dereference(): KeywordData = this
    fun documentation(doc: DocumentationRenderer) {


//        doc.append(ValkyrieHighlightColor.KEYWORD, "keyword ")
//        doc.append(ValkyrieHighlightColor.SYM_MACRO, name)
//        doc.append("<hr/>")
//        doc.append(detail)
    }


    companion object {
        fun builtinData(name: PsiElement): KeywordData? = when (name.elementType) {
//            ValkyrieTypes.KW_CLASS -> KeywordData(
//                "class",
//                """
//                class A()
//                """.trimIndent()
//            )
//            ValkyrieTypes.KW_TRAIT -> KeywordData(
//                "trait",
//                """
//                trait A()
//                """.trimIndent()
//            )
            else -> null
        }
    }
}


